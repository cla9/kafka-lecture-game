package com.example.kafkalectureupdown.game;

import com.example.kafkalectureupdown.exception.GameException;

class LoadingState implements GameState{
    @Override
    public void startGame(int life) {
        throw new GameException("게임이 이미 진행중입니다.");
    }

    @Override
    public GameManager.GameResultState play(String player, int value) {
        throw new GameException("다음판 게임은 아직 준비중입니다.");
    }

}
