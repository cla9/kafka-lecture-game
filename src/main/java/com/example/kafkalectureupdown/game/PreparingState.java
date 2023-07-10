package com.example.kafkalectureupdown.game;

import com.example.kafkalectureupdown.exception.GameException;
import com.example.kafkalectureupdown.service.ParticipantManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PreparingState implements GameState {
    private final ParticipantManager participantManager;
    private final GameManager gameManager;
    private final Logger logger;

    PreparingState(ParticipantManager participantManager, GameManager gameManager) {
        this.participantManager = participantManager;
        this.gameManager = gameManager;
        this.logger = LoggerFactory.getLogger(PreparingState.class);
    }

    @Override
    public void startGame(int life) {
        gameManager.setState(new PlayingState(participantManager, gameManager));
        gameManager.setLife(life);
        logger.debug("Change preparing state to playing state");
    }

    @Override
    public GameManager.GameResultState play(String player, int value) {
        throw new GameException("게임이 아직 진행중이지 않습니다.");
    }

}
