package com.example.kafkalectureupdown.exception;

public class AlreadyTopicExistenceException extends RuntimeException{
    public AlreadyTopicExistenceException() {
    }

    public AlreadyTopicExistenceException(String message) {
        super(message);
    }
}
