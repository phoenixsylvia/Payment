package com.example.payment.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonsException extends Exception {
    private final HttpStatus status;

    public CommonsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static CommonsException badRequest(String message) {
        return new CommonsException(message, HttpStatus.BAD_REQUEST);
    }
}
