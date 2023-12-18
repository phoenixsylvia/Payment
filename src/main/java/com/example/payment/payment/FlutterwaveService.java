package com.example.payment.payment;

import com.example.payment.exceptions.CommonsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FlutterwaveService {
    
    private final WebClient.Builder webClient;
    
    @Value("${FLWV_BASE_URL:}")
    private String flwwaveBaseUrl;

    @Value("${FLWV_SECRET_KEY:}")
    private String flutterwaveSecretKey;
    private String flwwaveEmail = "info@payment.com";

    @Value("${FLWV_SECRET_HASH:}")
    private String flutterwaveSecretHash;

    @Value("${PAYMENT_LOGO_URL:}")
    private String paymentLogoUrl;

    private String initiator = "PAYMENT";


    public PaymentInitiationResponse generatePaymentLink(PaymentRequestDto dto) throws CommonsException {
        try {
            FlutterwavePaymentLinkRequestDto linkRequestDto = buildPaymentLinkRequest(dto);
            FlutterwavePaymentLinkResponseDto response = webClient.build().post()
                    .uri(flwwaveBaseUrl + "/v3/payments")
                    .header("Authorization", "Bearer " + flutterwaveSecretKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(linkRequestDto)
                    .retrieve()
                    .bodyToMono(FlutterwavePaymentLinkResponseDto.class).block();
            if (response == null || !response.status.equalsIgnoreCase("success")) {
                throw new CommonsException("payment.link.bad_request", HttpStatus.BAD_REQUEST);
            }
            return response.toCardPaymentInitiationResponseDto(linkRequestDto);
        } catch (WebClientResponseException we) {
            if (we.getStatusCode().value() == 400) {
                log.error(we.getLocalizedMessage() + " : " + we.getStatusText() + " : " + we.getResponseBodyAsString());
                throw new CommonsException("payment.link.bad_request", HttpStatus.BAD_REQUEST);
            }

            throw new CommonsException("payment.link.failed", HttpStatus.BAD_REQUEST);
        }
    }

    public FlutterwavePaymentLinkRequestDto buildPaymentLinkRequest(PaymentRequestDto paymentRequestDto) {
        FlutterwavePaymentLinkRequestDto.Customer customer = new FlutterwavePaymentLinkRequestDto.Customer();
        String customerEmail = paymentRequestDto.getCustomer().getEmail();
        String email = StringUtils.isBlank(customerEmail) ? flwwaveEmail : customerEmail;
        customer.setEmail(email);
        customer.setName(paymentRequestDto.getCustomer().getName());

        Map<String, String> meta = new HashMap<>();
        meta.put("initiator", initiator);

        return getFlutterwavePaymentLinkRequestDto(paymentRequestDto, customer, meta);
    }

    private FlutterwavePaymentLinkRequestDto getFlutterwavePaymentLinkRequestDto(PaymentRequestDto paymentRequestDto, FlutterwavePaymentLinkRequestDto.Customer customer, Map<String, String> meta) {
        FlutterwavePaymentLinkRequestDto.Customizations customizations = new FlutterwavePaymentLinkRequestDto.Customizations();
        customizations.setLogo(paymentLogoUrl);

        FlutterwavePaymentLinkRequestDto dto = new FlutterwavePaymentLinkRequestDto();
        dto.setAmount(paymentRequestDto.getAmount());
        dto.setCurrency("NGN");
        dto.setCustomer(customer);
        dto.setCustomizations(customizations);
        dto.setMeta(meta);
        dto.setRedirectUrl(paymentRequestDto.getRedirectUrl());
        dto.setTxRef(paymentRequestDto.getPaymentReference());
        return dto;
    }

    public FlutterwavePaymentRequeryResponseDto getStatus(String paymentReference) throws CommonsException {
        try {
            FlutterwavePaymentRequeryResponseDto response = webClient.build().get()
                    .uri(flwwaveBaseUrl + "/v3/transactions/verify_by_reference?tx_ref=" + paymentReference)
                    .header("Authorization", "Bearer " + flutterwaveSecretKey)
                    .retrieve()
                    .bodyToMono(FlutterwavePaymentRequeryResponseDto.class)
                    .block();
            if (response == null || !response.status.equalsIgnoreCase("success")) {
                throw new CommonsException("payment.requery.failed", HttpStatus.BAD_REQUEST);
            }
            return response;
        } catch (WebClientResponseException we) {
            if (we.getStatusCode().value() == 404) {
                log.error(we.getLocalizedMessage() + " : " + we.getStatusText() + " : " + we.getResponseBodyAsString());
                throw new CommonsException("payment.requery.failed", HttpStatus.BAD_REQUEST);
            }
            throw new CommonsException("payment.requery.failed", HttpStatus.BAD_REQUEST);
        }
    }
}

