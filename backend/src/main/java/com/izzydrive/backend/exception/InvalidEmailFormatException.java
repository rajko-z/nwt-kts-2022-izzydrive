package com.izzydrive.backend.exception;

public class InvalidEmailFormatException extends Exception{

    public InvalidEmailFormatException(String message){
        super(message);
    }
}
