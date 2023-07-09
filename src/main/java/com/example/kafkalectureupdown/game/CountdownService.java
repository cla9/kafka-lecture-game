package com.example.kafkalectureupdown.game;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;


@Component
public class CountdownService {
    private final CountdownRunnable countdownRunnable;

    public CountdownService() {
        this.countdownRunnable = new CountdownRunnable();
    }

    public void countdown(Integer time, Consumer<Integer> consumer, Runnable expiredAlarmTrigger) {
        countdownRunnable.initialize(time,consumer,expiredAlarmTrigger);
        countdownRunnable.execute();
    }
}
