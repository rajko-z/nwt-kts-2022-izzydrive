package com.izzydrive.backend.exception;

public class InvalidPhoneNumberFormatException extends Exception{

    public InvalidPhoneNumberFormatException(String message){
        super(message);
    }
}
