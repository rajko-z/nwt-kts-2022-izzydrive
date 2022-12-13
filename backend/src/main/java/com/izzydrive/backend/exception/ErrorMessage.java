package com.izzydrive.backend.exception;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorMessage{

    private int statusCode;

    private Date timestamp;

    private String message;

    private int errorField;

    public ErrorMessage(int statusCode, Date timestamp, String message) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
    }
}
