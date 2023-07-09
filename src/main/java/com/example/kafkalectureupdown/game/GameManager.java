package com.example.kafkalectureupdown.game;

import com.example.kafkalectureupdown.exception.GameException;
import com.example.kafkalectureupdown.service.ParticipantManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {
    private AtomicInteger life;
    private AtomicInteger initialLife;
    private GameState gameState;
    private final ParticipantManager participantManager;
    private final CountdownService countdownService;
    private final Integer min;
    private final Integer max;
    private final List<Notification> notifications;

    public enum GameResultState {
        LOWER,
        GREATER,
        EQUAL
    }

    public GameManager(Integer min, Integer max, ParticipantManager participantManager, CountdownService countdownService) {
        this.min = min;
        this.max = max;
        this.countdownService = countdownService;
        this.life = new AtomicInteger(0);
        this.participantManager = participantManager;
        this.gameState = new PreparingState(participantManager, this);
        this.notifications = new ArrayList<>();
    }

    public void registerNotification(Notification notification) {
        notifications.add(notification);
    }

    void setState(GameState state) {
        this.gameState = state;
    }

    public Integer startGame(int life) {
        return gameState.startGame(life);
    }
    public void endGame(){
        participantManager.clearAllPlayers();
        setState(new PreparingState(participantManager, this));
    }

    public void join(String player) {
        participantManager.joinPlayer(player);
        notifications.forEach(n -> n.notifyParticipantJoined(player));
    }

    public GameResultState play(String player, int value) {
        final var result = gameState.play(player, value);
        if (result.equals(GameResultState.EQUAL)) {
            notifications.forEach(n -> n.notifyWinner(player, value, calcCurrentStage()));
            final var currentLife = decreaseLife();
            if (currentLife == 0) {
                setState(new PreparingState(participantManager, this));
                notifyGameOver();
            } else {
                setState(new LoadingState());
                executeLoadingAlarmTimer();
            }
        }
        return result;
    }

    private void executeLoadingAlarmTimer() {
        countdownService.countdown(10, countdown -> notifications.forEach(n -> n.notifyCountdown(countdown)),
                () -> {
                    notifyGameStart();
                    setState(new PlayingState(participantManager, this));
                });
    }

    void setLife(int life) {
        if (life <= 0) {
            throw new GameException("life는 양의 정수 값만 입력 가능합니다.");
        }
        this.life = new AtomicInteger(life);
        this.initialLife = new AtomicInteger(life);
        notifyGameStart();
    }

    private void notifyGameStart() {
        notifications.forEach(n -> n.notifyGameStart(calcCurrentStage()));
    }
    private void notifyGameOver(){
        notifications.forEach(Notification::notifyGameOver);
    }

    private int decreaseLife() {
        return this.life.decrementAndGet();
    }

    private int calcCurrentStage() {
        return initialLife.get() - life.get() + 1;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
