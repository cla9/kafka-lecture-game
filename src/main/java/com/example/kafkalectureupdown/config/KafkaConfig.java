package com.example.kafkalectureupdown.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public AdminClient adminClient(KafkaProperties properties){
        return AdminClient.create(properties.buildAdminProperties());
    }

    @Bean
    public NewTopic gameTopic(){
        return TopicBuilder.name("game")
                .partitions(3)
                .build();
    }



}
