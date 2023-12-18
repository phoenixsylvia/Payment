package com.example.payment.controller;

import com.example.payment.dto.CustomUser;
import com.example.payment.exceptions.CommonsException;
import com.example.payment.payment.PaymentLogDto;
import com.example.payment.payment.PaymentRequestDto;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(Authentication authentication, @RequestBody PaymentRequestDto paymentRequestDto) throws CommonsException {
        long userId = CustomUser.getId(authentication);
        return new ResponseEntity<>(paymentService.initiatePayment(userId, paymentRequestDto), HttpStatus.OK);
    }

    @GetMapping("/status/{reference}")
    public ResponseEntity<?> getPaymentStatus(
            @PathVariable String reference) throws CommonsException {
        PaymentLogDto paymentLogDto = paymentService.getPayment(reference);
        return new ResponseEntity<>(paymentLogDto, HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(Authentication authentication,@RequestParam String page, @RequestParam String size) {
        long userId = CustomUser.getId(authentication);
        return new ResponseEntity<>(paymentService.transactions(userId, Integer.parseInt(page), Integer.parseInt(size)), HttpStatus.OK);

    }
}
