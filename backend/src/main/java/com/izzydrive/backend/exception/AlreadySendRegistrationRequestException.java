package com.izzydrive.backend.exception;

public class AlreadySendRegistrationRequestException extends BadRequestException{
    public AlreadySendRegistrationRequestException(String message) {
        super(message);
    }
}
