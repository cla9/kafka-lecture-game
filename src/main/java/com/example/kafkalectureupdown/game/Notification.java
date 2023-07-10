package com.example.kafkalectureupdown.game;

public interface Notification {
    default void notifyParticipantJoined(String player){}
    default void notifyGameStart(Integer stage){}
    default void notifyPlayerTry(String player){}
    default void notifyWinner(String winner, Integer answer, Integer stage){}
    default void notifyCountdown(Integer countdown){}
    default void notifyGameOver(){}
}
