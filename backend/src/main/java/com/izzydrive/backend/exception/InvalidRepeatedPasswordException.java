package com.izzydrive.backend.exception;

public class InvalidRepeatedPasswordException extends BadRequestException{

    public InvalidRepeatedPasswordException(String message){
        super(message);
    }
}
