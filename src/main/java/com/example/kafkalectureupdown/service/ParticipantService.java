package com.example.kafkalectureupdown.service;

import com.example.kafkalectureupdown.exception.AlreadyTopicExistenceException;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class ParticipantService implements ParticipantManager {
    private final AdminClient kafkaAdmin;
    private final ConcurrentSkipListSet<String> playerTopics;
    private final Set<String> createdTopics;

    public ParticipantService(AdminClient kafkaAdmin) throws ExecutionException, InterruptedException, TimeoutException {
        this.kafkaAdmin = kafkaAdmin;
        playerTopics = new ConcurrentSkipListSet<>();
        createdTopics = kafkaAdmin.listTopics()
                .names()
                .get(10_000, TimeUnit.SECONDS);
    }


    @Override
    public void joinPlayer(String player) {
        if(!hasTopic(player)){
            if(!createdTopics.contains(player)){
                kafkaAdmin.createTopics(List.of(TopicBuilder.name(player).build()));
            }
            playerTopics.add(player);
        }else{
            throw new AlreadyTopicExistenceException("동일한 nick-name을 지닌 사용자가 이미 Game에 참가했습니다.");
        }
    }

    @Override
    public boolean hasPlayer(String player) {
        return playerTopics.contains(player);
    }


    @Override
    public int getPlayerCount() {
        return playerTopics.size();
    }

    @Override
    public void clearAllPlayers() {
        playerTopics.clear();
    }

    public List<String> getPlayers(){ return playerTopics.stream().toList();}

    private boolean hasTopic(String name){
        return playerTopics.contains(name);
    }


}
