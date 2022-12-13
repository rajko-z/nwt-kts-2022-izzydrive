package com.izzydrive.backend.exception;

public class InvalidCredentialsException extends AppException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException(String message, int errorField) {
        super(message, errorField);
    }
}
