package com.example.payment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class CreateUserRequest {

    @NotBlank(message = "Firstname must not be blank")
    @NotNull(message = "Firstname must not be null")
    private String firstName;

    @NotBlank(message = "Lastname must not be blank")
    @NotNull(message = "Lastname must not be null")
    private String lastName;

    @NotBlank(message = "Email must not be blank")
    @NotNull(message = "Email must not be null")
    @Email
    private String email;

    @NotBlank(message = "Phone number must not be blank")
    @NotNull(message = "Phone number must not be null")
    private String phoneNumber;

    @NotBlank(message = "Password must not be blank")
    @NotNull(message = "Password must not be null")
    private String password;
}
