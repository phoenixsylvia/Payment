package com.example.payment.service;

import com.example.payment.dto.CreateUserRequest;
import com.example.payment.dto.UserDto;
import com.example.payment.exceptions.CommonsException;

public interface UserService {
    UserDto createUser(CreateUserRequest createUserRequest) throws CommonsException;
}
