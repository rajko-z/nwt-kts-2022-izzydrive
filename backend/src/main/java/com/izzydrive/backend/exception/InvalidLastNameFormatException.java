package com.izzydrive.backend.exception;

public class InvalidLastNameFormatException extends BadRequestException{

    public InvalidLastNameFormatException(String message){
        super(message);
    }
}
