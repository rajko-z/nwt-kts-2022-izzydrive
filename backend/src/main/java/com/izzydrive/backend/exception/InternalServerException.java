package com.izzydrive.backend.exception;

public class InternalServerException extends AppException {

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, int errorField) {
        super(message, errorField);
    }
}
