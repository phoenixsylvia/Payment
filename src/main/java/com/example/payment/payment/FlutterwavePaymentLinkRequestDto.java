package com.example.payment.payment;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class FlutterwavePaymentLinkRequestDto {
    @JsonAlias("txRef")
    @JsonProperty("tx_ref")
    public String txRef;
    public BigDecimal amount;
    public String currency;
    @JsonAlias("redirectUrl")
    @JsonProperty("redirect_url")
    public String redirectUrl;
    public Map<String, String> meta;
    public Customer customer;
    public Customizations customizations;


    @JsonProperty("payment_plan")
    private Integer paymentPlan;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Customer {
        public String email;
        @JsonAlias("phoneNumber")
        @JsonProperty("phonenumber")
        public String phoneNumber;
        public String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Customizations {
        public String title;
        public String logo;
    }
}
