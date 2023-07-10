package com.example.kafkalectureupdown.game;

import com.example.kafkalectureupdown.exception.GameException;
import com.example.kafkalectureupdown.service.ParticipantManager;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class PlayingState implements GameState{
    private final ParticipantManager participantManager;
    private final AtomicInteger goal;
    private final Integer min;
    private final Integer max;

    public PlayingState(ParticipantManager participantManager, GameManager gameManager) {
        this.participantManager = participantManager;
        this.min = gameManager.getMin();
        this.max = gameManager.getMax();
        this.goal = new AtomicInteger(new Random().ints(min, max).findFirst().getAsInt());
    }

    @Override
    public void startGame(int life) {
        throw new GameException("게임이 이미 진행중입니다.");
    }

    @Override
    public GameManager.GameResultState play(String player, int value) {
        verifyPlayerJoinedGame(player);
        var v = goal.intValue();
        if (v == value)     return GameManager.GameResultState.EQUAL;
        else if (v > value) return GameManager.GameResultState.GREATER;
        else                return GameManager.GameResultState.LOWER;
    }


    private void verifyPlayerJoinedGame(String player) {
        if(!participantManager.hasPlayer(player)){
            throw new GameException("join 문구를 입력하여 게임에 먼저 참여하십시오.");
        }
    }
}
