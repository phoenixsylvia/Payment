package com.example.payment.payment;

import com.example.payment.enums.PaymentCurrency;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashMap;

@Setter
@Getter
public class PaymentInitiationRequestDto {

    private BigDecimal amount;
    private PaymentCurrency currencyCode;
    @NotBlank(message = "country_code.not_blank")
    private String countryCode;
    private String source;
    private String transactionReference;
    private String paymentReference;
    private String paymentPlanId;
    private HashMap<String, String> extras;
}
