package com.example.kafkalectureupdown.service;

import com.example.kafkalectureupdown.game.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static com.example.kafkalectureupdown.game.GameManager.GameResultState.EQUAL;
import static com.example.kafkalectureupdown.game.GameManager.GameResultState.GREATER;

@Component
public class GameListenerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final GameManager gameManager;
    private final Pattern pattern = Pattern.compile("^[1-9][0-9]{0,4}$");

    public GameListenerService(KafkaTemplate<String, String> kafkaTemplate, GameManager gameManager) {
        this.kafkaTemplate = kafkaTemplate;
        this.gameManager = gameManager;
    }

    @KafkaListener(id = "game", topics = "game", concurrency = "3", errorHandler = "kafkaGameListenerErrorHandler")
    public void listen(String value, @Header("nick-name") String nickName) {

        if (value.equals("join")) {
            gameManager.join(nickName);
            kafkaTemplate.send(nickName, "Game에 참가하였습니다.");
            return;
        }

        if (!isInteger(value)) {
            kafkaTemplate.send(nickName, "join 문구 이외에 다른 문자열은 허용되지 않습니다.");
            return;
        }

        var score = Integer.parseInt(value);
        int min = gameManager.getMin();
        int max = gameManager.getMax();

        if (score < min || score > max) {
            kafkaTemplate.send(nickName, min + "~" + max + " 사이 양의 정수 값을 입력하셔야 합니다.");
            return;
        }

        final var result = gameManager.play(nickName, score);

        if (!result.equals(EQUAL)) {
            final var message = nickName + " 님이 입력하신 " + score + "보다 " + (result.equals(GREATER) ? "큽니다." : "작습니다.");
            kafkaTemplate.send(nickName, message);
        }
    }

    private boolean isInteger(String value) {
        return pattern.matcher(value).matches();
    }
}
