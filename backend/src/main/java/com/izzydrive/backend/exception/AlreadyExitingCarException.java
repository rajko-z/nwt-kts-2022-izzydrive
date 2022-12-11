package com.izzydrive.backend.exception;

public class AlreadyExitingCarException extends BadRequestException{

    public AlreadyExitingCarException(String message) {
        super(message);
    }
}
