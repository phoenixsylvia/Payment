package com.example.payment.payment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class PaymentRequestDto  extends PaymentInitiationRequestDto {

    private String redirectUrl;


    private BigDecimal amount;

    private Customer customer;
}
