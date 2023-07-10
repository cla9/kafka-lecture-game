package com.example.kafkalectureupdown.game;

public interface GameState {
    void startGame(int life);
    GameManager.GameResultState play(String player, int value);
}
