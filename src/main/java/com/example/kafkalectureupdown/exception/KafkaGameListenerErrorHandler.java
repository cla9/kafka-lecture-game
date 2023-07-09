package com.example.kafkalectureupdown.exception;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class KafkaGameListenerErrorHandler implements KafkaListenerErrorHandler {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaGameListenerErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        final var topic = new String(Objects.requireNonNull(message.getHeaders().get("nick-name", byte[].class)));
        final var messsage = exception.getCause().getMessage();
        kafkaTemplate.send(topic, messsage);
        return null;
    }

}
