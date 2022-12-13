package com.izzydrive.backend.exception;

public class AppException extends RuntimeException {

    private String message;

    private int errorField;

    public AppException(String message) {
        super();
        this.message = message;
    }

    public AppException(String message, int errorField) {
        super();
        this.message = message;
        this.errorField = errorField;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorField() {
        return errorField;
    }

    public void setErrorField(int errorField) {
        this.errorField = errorField;
    }
}
