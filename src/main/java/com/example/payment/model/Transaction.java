package com.example.payment.model;

import com.example.payment.enums.PaymentCurrency;
import com.example.payment.enums.PaymentStatus;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

@Setter
@Getter
@Entity(name = "transactions")
@Table(indexes = @Index(columnList = "id,userId"))
@ToString
public class Transaction extends Base {

    private String reference;

    private String source;

    @Enumerated(EnumType.STRING)
    private PaymentStatus Status;

    private String paymentLink;

    private BigDecimal amount;

    private String processor;

    private String processorReference;

    private String transactionReference;

    @Enumerated(EnumType.STRING)
    private PaymentCurrency paymentCurrency;

    @Column(nullable = false)
    private long userId;

    @PrePersist
    public void appendReference(){
        this.reference = RandomStringUtils.randomAlphanumeric(6, 10);
    }

    public String getReference(){
        return String.format("%s%s%s", this.getId(), "_", this.reference);
    }
}
