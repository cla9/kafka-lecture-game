package com.example.kafkalectureupdown.game;

import com.example.kafkalectureupdown.exception.GameException;
import com.example.kafkalectureupdown.exception.MissingArgumentException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CountdownRunnable implements Runnable {
    private Integer time;
    private Consumer<Integer> consumer;
    private Runnable expiredAlarmTrigger;
    private ScheduledExecutorService scheduler;


    @Override
    public void run() {
        consumer.accept(time--);
        if(time < 0){
            expiredAlarmTrigger.run();
            scheduler.shutdown();
        }
    }

    public void initialize(Integer time, Consumer<Integer> consumer, Runnable expiredAlarmTrigger){
        this.time = time;
        this.consumer = consumer;
        this.expiredAlarmTrigger = expiredAlarmTrigger;
    }

    public void execute(){
        if(this.time == null || this.consumer == null || this.expiredAlarmTrigger == null){
            throw new MissingArgumentException("인자를 반드시 입력해야합니다.");
        }

        if(scheduler != null && !scheduler.isShutdown()){
            throw new GameException("이미 수행중인 타이머가 존재합니다.");
        }

        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::run, 0, 1, TimeUnit.SECONDS);
    }
}
