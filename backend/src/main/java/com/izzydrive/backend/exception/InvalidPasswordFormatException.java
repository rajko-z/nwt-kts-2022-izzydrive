package com.izzydrive.backend.exception;

public class InvalidPasswordFormatException extends Exception{

    public InvalidPasswordFormatException(String message){
        super(message);
    }
}
