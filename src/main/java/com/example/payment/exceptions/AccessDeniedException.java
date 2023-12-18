package com.example.payment.exceptions;

import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {

    private final String message;

    public AccessDeniedException(String message) {
        super(message);
        this.message = message;
    }
}
