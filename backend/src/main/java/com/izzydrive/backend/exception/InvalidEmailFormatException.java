package com.izzydrive.backend.exception;

public class InvalidEmailFormatException extends BadRequestException{

    public InvalidEmailFormatException(String message){
        super(message);
    }


}
