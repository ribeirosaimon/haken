package com.example.hacken.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorResponse {
    private String message;
    private Date date;

    public ErrorResponse(String message) {
        this.message = message;
    }
}