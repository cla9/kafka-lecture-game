package com.example.kafkalectureupdown.service;

import com.example.kafkalectureupdown.game.GameManager;
import com.example.kafkalectureupdown.game.Notification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BroadcastService implements Notification {
    private final ParticipantService participantService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BroadcastService(ParticipantService participantService, GameManager gameManager, KafkaTemplate<String, String> kafkaTemplate) {
        this.participantService = participantService;
        this.kafkaTemplate = kafkaTemplate;
        gameManager.registerNotification(this);
    }

    @Override
    public void notifyGameStart(Integer stage) {
        broadcast("\n\n========== " + stage + " 번째 게임을 시작합니다. ========== \n\n");
    }

    @Override
    public void notifyWinner(String winner, Integer answer, Integer stage) {
        broadcast("\n\n========== " + stage + " 번째 승자는 " + winner + "입니다. 정답은 " + answer + "입니다. ==========\n\n");
    }

    @Override
    public void notifyCountdown(Integer countdown) {
        broadcast(countdown+"초 이후에 게임이 시작됩니다.");
    }

    @Override
    public void notifyGameOver() {
        broadcast("\n\n========== Game을 종료합니다. ========== \n\n");
    }

    private void broadcast(String message){
        participantService.getPlayers()
                .forEach(player -> kafkaTemplate.send(player, message));
    }
}
