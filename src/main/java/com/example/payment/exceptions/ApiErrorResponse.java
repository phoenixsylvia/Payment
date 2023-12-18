package com.example.payment.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private Integer errorCode;
    private String errorMessage;
    private Date date;


}
