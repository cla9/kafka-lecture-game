package com.example.kafkalectureupdown.service;

public interface ParticipantManager {
    void joinPlayer(String player);
    boolean hasPlayer(String player);
    int getPlayerCount();
    void clearAllPlayers();
}
