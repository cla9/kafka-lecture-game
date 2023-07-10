package com.example.kafkalectureupdown.game;

public class WinnerInfo {
    private String player;
    private int count;
    private int totalScore;
    private int totalTime;
    private int totalTryCount;

    public WinnerInfo(String player , int totalScore, int totalTime, int totalTryCount) {
        this.count = 1;
        this.player = player;
        this.totalScore = totalScore;
        this.totalTime = totalTime;
        this.totalTryCount = totalTryCount;
    }

    public void accumulate(int score, int time, int count) {
        this.count++;
        this.totalScore += score;
        this.totalTime += time;
        this.totalTryCount += count;
    }

    public Winner getWinner(){
        return new Winner(player, count, totalScore, getAverageTime(), getAverageTryCount());
    }

    private int getAverageTime(){
        return totalTime / count;
    }
    private int getAverageTryCount(){
        return totalTryCount / count;
    }

}
