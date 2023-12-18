package com.example.payment.controller;

import com.example.payment.security.TokenProvider;
import com.example.payment.dto.CreateUserRequest;
import com.example.payment.dto.LoginDto;
import com.example.payment.dto.LoginResponse;
import com.example.payment.dto.UserDto;
import com.example.payment.exceptions.CommonsException;
import com.example.payment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody CreateUserRequest userDto) throws CommonsException {
        UserDto response = userService.createUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        LoginResponse response = new LoginResponse(tokenProvider.generateJWTToken(authentication));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
