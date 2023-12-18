package com.example.payment.payment;

import com.example.payment.enums.PaymentCurrency;
import com.example.payment.enums.PaymentStatus;
import com.example.payment.model.Transaction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class PaymentLogDto {

    private  String reference;

    private long userId;

    private  String source;

    private PaymentStatus status;

    private BigDecimal amount;

    private String transactionId;

    private PaymentCurrency paymentCurrency;

    public static PaymentLogDto fromPaymentLog(Transaction transaction) {
        PaymentLogDto dto = new PaymentLogDto();
        BeanUtils.copyProperties(transaction, dto);
        dto.setUserId(transaction.getUserId());
        dto.setTransactionId(transaction.getTransactionReference());
        return  dto;
    }
}
