package com.example.payment.payment;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString
public class FlutterwavePaymentRequeryResponseDto {

    public String status;
    public String message;

    public Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Data {
        public int id;
        @JsonAlias("txRef")
        @JsonProperty("tx_ref")
        public String txRef;
        @JsonAlias("flwRef")
        @JsonProperty("flw_ref")
        public String flwRef;
        @JsonAlias("deviceFingerprint")
        @JsonProperty("device_fingerprint")
        public String deviceFingerprint;
        public BigDecimal amount;
        public String currency;
        @JsonAlias("chargedAmount")
        @JsonProperty("charged_amount")
        public BigDecimal chargedAmount;
        @JsonAlias("appFee")
        @JsonProperty("app_fee")
        public BigDecimal appFee;
        @JsonAlias("merchantFee")
        @JsonProperty("merchant_fee")
        public BigDecimal merchantFee;
        @JsonAlias("processorResponse")
        @JsonProperty("processor_response")
        public String processorResponse;
        @JsonAlias("authModel")
        @JsonProperty("auth_model")
        public String authModel;
        public String ip;
        public String narration;
        public String status;
        @JsonAlias("paymentType")
        @JsonProperty("payment_type")
        public String paymentType;
        @JsonAlias("createdAt")
        @JsonProperty("created_at")
        public Date createdAt;
        @JsonAlias("accountId")
        @JsonProperty("account_id")
        public int accountId;

    }
}
