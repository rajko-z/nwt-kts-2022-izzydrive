package com.izzydrive.backend.exception;

public class InvalidFirstNameFormatException extends BadRequestException{

    public InvalidFirstNameFormatException(String message) {
        super(message);
    }
}
