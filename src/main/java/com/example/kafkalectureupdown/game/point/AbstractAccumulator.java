package com.example.kafkalectureupdown.game.point;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAccumulator implements Accumulator{

    protected final List<Point> points;

    protected AbstractAccumulator() {
        points = new ArrayList<>();
    }


    @Override
    public Integer calculate(Integer base) {
        return points.get(lowerBound(base, points)).point();
    }

    protected Integer lowerBound(Integer key, List<Point> list){
        int lowerBound = 0;
        while(lowerBound < list.size()){
            if(key > list.get(lowerBound).base()){
                lowerBound++;
            }else{
                return lowerBound;
            }
        }
        return lowerBound;
    }
}
