package com.example.kafkalectureupdown.controller;


import com.example.kafkalectureupdown.config.GameConst;
import com.example.kafkalectureupdown.dto.GameStartResponseDto;
import com.example.kafkalectureupdown.game.GameManager;
import com.example.kafkalectureupdown.service.GameListenerService;
import com.example.kafkalectureupdown.service.ParticipantService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class GameRestController {
    private final ParticipantService participantService;
    private final GameManager gameManager;
    private final KafkaListenerEndpointRegistry listenerEndpointRegistry;
    private final GameListenerService gameListenerService;

    public GameRestController(ParticipantService participantService, GameManager gameManager, KafkaListenerEndpointRegistry listenerEndpointRegistry, GameListenerService gameListenerService) {
        this.participantService = participantService;
        this.gameManager = gameManager;
        this.listenerEndpointRegistry = listenerEndpointRegistry;
        this.gameListenerService = gameListenerService;
    }

    @GetMapping("/start")
    public ResponseEntity<GameStartResponseDto> start(@RequestParam("life") Integer life) {
        final var playerCount = this.gameManager.startGame(life);
        Objects.requireNonNull(listenerEndpointRegistry.getListenerContainer(GameConst.TOPIC)).start();
        return ResponseEntity.ok(new GameStartResponseDto(playerCount));
    }

    @GetMapping("/stop")
    public ResponseEntity<Void> end(){
        this.gameManager.endGame();
        Objects.requireNonNull(listenerEndpointRegistry.getListenerContainer(GameConst.TOPIC)).stop();
        return ResponseEntity.noContent().build();
    }
}
