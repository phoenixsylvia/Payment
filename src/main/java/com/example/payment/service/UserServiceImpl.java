package com.example.payment.service;

import com.example.payment.dto.CreateUserRequest;
import com.example.payment.dto.UserDto;
import com.example.payment.enums.Role;
import com.example.payment.exceptions.CommonsException;
import com.example.payment.model.User;
import com.example.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(CreateUserRequest createUserRequest) throws CommonsException {
        if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent())
            throw new CommonsException("Email already exists", HttpStatus.BAD_REQUEST);
        User user = new User();
        BeanUtils.copyProperties(createUserRequest, user);
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole(Role.USER);
        user = userRepository.save(user);
        log.info("user created [{}]", user);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;

    }
}