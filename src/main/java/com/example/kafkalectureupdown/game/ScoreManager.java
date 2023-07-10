package com.example.kafkalectureupdown.game;

import com.example.kafkalectureupdown.game.point.Accumulator;
import com.example.kafkalectureupdown.game.point.TimePointAccumulator;
import com.example.kafkalectureupdown.game.point.TryCountAccumulator;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ScoreManager {
    private final Map<String, WinnerInfo> scores;
    private final Map<Integer, Long> stageTimeMap;
    private final Map<String, Integer> playerTryCountMap;
    private final Accumulator tryCountAccumulator;
    private final Accumulator timePointAccumulator;

    public ScoreManager() {
        scores = new HashMap<>();
        stageTimeMap = new HashMap<>();
        playerTryCountMap = new HashMap<>();
        tryCountAccumulator = new TryCountAccumulator();
        timePointAccumulator = new TimePointAccumulator();
    }

    public void accumulatePlayerTryCount(String player){
        if(playerTryCountMap.containsKey(player)){
            final var tryCount = playerTryCountMap.get(player);
            playerTryCountMap.put(player, tryCount+1);
        }else{
            playerTryCountMap.put(player, 1);
        }
    }

    public void setGameStartTime(Integer stage){
        stageTimeMap.put(stage, System.currentTimeMillis());
    }

    public void calculateWinnerPoint(String winner, Integer stage) {
        int time = (int)(System.currentTimeMillis() - stageTimeMap.get(stage)) / 1000;
        int count = playerTryCountMap.get(winner);

        int timePoint = timePointAccumulator.calculate(time);
        int tryCountPoint = tryCountAccumulator.calculate(count);

        final var winnerPoint = 100 + tryCountPoint + timePoint;
        if(scores.containsKey(winner)){
            final var winnerInfo = scores.get(winner);
            winnerInfo.accumulate(winnerPoint, time, count);
        }else{
            scores.put(winner, new WinnerInfo(winner, winnerPoint, time, count));
        }

        playerTryCountMap.clear();
    }

    public List<Winner> getWinners(){
        return scores.values().stream().map(WinnerInfo::getWinner)
                .sorted(Comparator.comparing(Winner::score).reversed())
                .collect(Collectors.toList());
    }

    public void clear(){
        this.scores.clear();
        this.stageTimeMap.clear();
        this.playerTryCountMap.clear();
    }
}
