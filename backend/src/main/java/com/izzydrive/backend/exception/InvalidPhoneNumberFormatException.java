package com.izzydrive.backend.exception;

public class InvalidPhoneNumberFormatException extends BadRequestException{

    public InvalidPhoneNumberFormatException(String message){
        super(message);
    }
}
