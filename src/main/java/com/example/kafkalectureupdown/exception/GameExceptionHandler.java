package com.example.kafkalectureupdown.exception;

import com.example.kafkalectureupdown.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GameExceptionHandler {
    @ExceptionHandler(GameException.class)
    protected ResponseEntity<ErrorResponse> handleGameException(GameException e){
        final var error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
