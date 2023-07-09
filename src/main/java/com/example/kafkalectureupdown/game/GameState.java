package com.example.kafkalectureupdown.game;

public interface GameState {
    Integer startGame(int life);
    GameManager.GameResultState play(String player, int value);
}
