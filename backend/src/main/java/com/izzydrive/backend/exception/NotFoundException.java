package com.izzydrive.backend.exception;

public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, int errorField) {
        super(message, errorField);
    }
}
