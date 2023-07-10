package com.example.kafkalectureupdown.service;

import com.example.kafkalectureupdown.game.GameManager;
import com.example.kafkalectureupdown.game.Notification;
import com.example.kafkalectureupdown.game.ScoreManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class GameAdminService implements Notification {
    private final Logger logger;
    private final ScoreManager scoreManager;
    private final BroadcastService broadcastService;

    public GameAdminService(GameManager gameManager, ScoreManager scoreManager, BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
        gameManager.registerNotification(this);
        logger = LoggerFactory.getLogger(GameAdminService.class);
        this.scoreManager = scoreManager;
    }

    @Override
    public void notifyPlayerTry(String player) {
        this.scoreManager.accumulatePlayerTryCount(player);
    }

    @Override
    public void notifyParticipantJoined(String player) {
        logger.info("신규 Player " + player +"님이 Game에 참여하였습니다.");
    }

    @Override
    public void notifyGameStart(Integer stage) {
        logger.info(stage + "번 째 게임이 시작됩니다");
        this.scoreManager.setGameStartTime(stage);
    }

    @Override
    public synchronized void notifyWinner(String winner, Integer answer, Integer stage) {
        logger.info("정답은 " + answer + "입니다. " + stage + "게임 승자는 " + winner + "님 입니다.");
        this.scoreManager.calculateWinnerPoint(winner, stage);
    }

    @Override
    public void notifyGameOver() {
        logger.info("모든 게임이 종료되었습니다.");
        logger.info("========= 게임 결과 =========");
        final var winners = this.scoreManager.getWinners();
        final var grade = new AtomicInteger(0);
        final var results = winners.stream()
                .map(winner -> String.format("순위 : %2d\n정답자 : %10s\n정답 횟수 : %2d\n전체 점수 : %4d\n평균 정답 시간(s) : %3d\n평균 정답 시도 횟수 : %3d", grade.incrementAndGet(), winner.player(), winner.count(), winner.score(), winner.averageTime(), winner.averageTryCount()))
                .collect(Collectors.joining("\n\n\n"));

        final var builder = new StringBuilder();
        builder.append("=== 게임 순위 ===\n")
               .append(results)
               .append("\n=== 축하합니다!!! ===")
               .append("\n\n=== Game을 종료합니다. === \n\n");

        broadcastService.broadcast(builder.toString());
        winners.forEach(winner -> logger.info(String.format("정답자 : %15s , 정답 횟수 : %2d, 전체 점수 : %4d, 평균 정답 시간(s) : %3d, 평균 정답 시도 횟수 : %3d" ,winner.player(), winner.count(), winner.score(), winner.averageTime(), winner.averageTryCount())));
        this.scoreManager.clear();
        logger.info("==================");
    }
}
