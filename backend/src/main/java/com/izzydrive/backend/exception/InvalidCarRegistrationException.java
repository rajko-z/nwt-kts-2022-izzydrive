package com.izzydrive.backend.exception;

public class InvalidCarRegistrationException extends BadRequestException{

    public InvalidCarRegistrationException(String message) {
        super(message);
    }

}

