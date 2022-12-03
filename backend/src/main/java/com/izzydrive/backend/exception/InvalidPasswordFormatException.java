package com.izzydrive.backend.exception;

public class InvalidPasswordFormatException extends BadRequestException{

    public InvalidPasswordFormatException(String message){
        super(message);
    }
}
