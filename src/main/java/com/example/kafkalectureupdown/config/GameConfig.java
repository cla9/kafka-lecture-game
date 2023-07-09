package com.example.kafkalectureupdown.config;

import com.example.kafkalectureupdown.game.CountdownService;
import com.example.kafkalectureupdown.game.GameManager;
import com.example.kafkalectureupdown.service.ParticipantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GameConfig {

    @Value("${spring.game.min}")
    private Integer min;

    @Value("${spring.game.max}")
    private Integer max;

    @Bean
    public GameManager gameController(ParticipantService participantService, CountdownService countdownService){
        return new GameManager(min, max, participantService, countdownService);
    }
}
