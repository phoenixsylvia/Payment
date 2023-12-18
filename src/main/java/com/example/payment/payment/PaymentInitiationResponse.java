package com.example.payment.payment;


import com.example.payment.enums.PaymentCurrency;
import com.example.payment.enums.PaymentStatus;
import com.example.payment.enums.Processor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class PaymentInitiationResponse {
    private String paymentLink;
    private BigDecimal amount;
    private String paymentReference;
    private PaymentCurrency currency;
    private PaymentStatus status;
    private String processorReference;
    private Processor processor;
}
