package com.example.payment.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CommonsException {

    public UserNotFoundException() {
        super("user.not_found", HttpStatus.NOT_FOUND);
    }
}
