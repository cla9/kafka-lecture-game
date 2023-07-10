package com.example.kafkalectureupdown.controller;


import com.example.kafkalectureupdown.config.GameConst;
import com.example.kafkalectureupdown.dto.GameStartResponseDto;
import com.example.kafkalectureupdown.dto.ParticipantResponseDto;
import com.example.kafkalectureupdown.game.GameManager;
import com.example.kafkalectureupdown.service.ParticipantService;
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


    public GameRestController(ParticipantService participantService, GameManager gameManager, KafkaListenerEndpointRegistry listenerEndpointRegistry) {
        this.participantService = participantService;
        this.gameManager = gameManager;
        this.listenerEndpointRegistry = listenerEndpointRegistry;
    }

    @GetMapping("/start")
    public ResponseEntity<GameStartResponseDto> start(@RequestParam("life") Integer life) {
        this.gameManager.startGame(life);
        Objects.requireNonNull(listenerEndpointRegistry.getListenerContainer(GameConst.TOPIC)).start();
        return ResponseEntity.ok(new GameStartResponseDto(participantService.getPlayerCount()));
    }

    @GetMapping("/participants")
    public ResponseEntity<ParticipantResponseDto> participants(){
        return ResponseEntity.ok(new ParticipantResponseDto(participantService.getPlayerCount(), participantService.getPlayers()));
    }

    @GetMapping("/stop")
    public ResponseEntity<Void> end(){
        this.gameManager.endGame();
        Objects.requireNonNull(listenerEndpointRegistry.getListenerContainer(GameConst.TOPIC)).stop();
        return ResponseEntity.noContent().build();
    }
}
