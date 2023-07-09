package com.example.kafkalectureupdown.service;

import com.example.kafkalectureupdown.game.GameManager;
import com.example.kafkalectureupdown.game.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@Component
public class GameAdminService implements Notification {
    private final Logger logger;
    private final Map<String, Integer> scores;

    public GameAdminService(GameManager gameManager) {
        gameManager.registerNotification(this);
        logger = LoggerFactory.getLogger(GameAdminService.class);
        scores = new TreeMap<>(Collections.reverseOrder());
    }

    @Override
    public void notifyParticipantJoined(String player) {
        logger.info("신규 Player " + player +"님이 Game에 참여하였습니다.");
    }

    @Override
    public void notifyGameStart(Integer stage) {
        logger.info(stage + "번 째 게임이 시작됩니다");
    }

    @Override
    public void notifyWinner(String winner, Integer answer, Integer stage) {
        logger.info("정답은 " + answer + "입니다. " + stage + "게임 승자는 " + winner + "님 입니다.");
        if(scores.containsKey(winner)){
            final var point = scores.get(winner);
            scores.put(winner, point+1);
        }else{
            scores.put(winner, 1);
        }
    }

    @Override
    public void notifyGameOver() {
        logger.info("모든 게임이 종료되었습니다.");
        logger.info("========= 게임 결과 =========");
        scores.forEach((winner, point) -> logger.info(winner + " " + point));
        logger.info("==================");
        scores.clear();
    }
}
