package com.example.payment.payment;

import com.example.payment.enums.PaymentStatus;
import com.example.payment.enums.Processor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class FlutterwavePaymentLinkResponseDto {

    public String status;
    public String message;
    public Data data;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public class Data {
        public String link;
    }

    public PaymentInitiationResponse toCardPaymentInitiationResponseDto(FlutterwavePaymentLinkRequestDto requestDto){
        PaymentInitiationResponse dto = new PaymentInitiationResponse();
        dto.setPaymentLink(this.data.getLink());
        dto.setStatus(PaymentStatus.PENDING);
        dto.setProcessor(Processor.FLUTTERWAVE);
        dto.setPaymentReference(requestDto.getTxRef());
        dto.setAmount(requestDto.getAmount());
        dto.setProcessorReference(requestDto.getTxRef());//processor reference is our trnxRef
        return dto;
    }
}
