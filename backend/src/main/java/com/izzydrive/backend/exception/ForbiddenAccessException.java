package com.izzydrive.backend.exception;

public class ForbiddenAccessException extends AppException {

    public ForbiddenAccessException(String message) {
        super(message);
    }
}
