package com.example.payment.service;


import com.example.payment.dto.PageDto;
import com.example.payment.enums.PaymentCurrency;
import com.example.payment.enums.PaymentStatus;
import com.example.payment.exceptions.CommonsException;
import com.example.payment.model.Transaction;
import com.example.payment.model.User;
import com.example.payment.payment.*;
import com.example.payment.repository.TransactionRepository;
import com.example.payment.repository.UserRepository;
import com.example.payment.utils.IAppendableReferenceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final FlutterwaveService flutterwaveService;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    @Transactional
    public PaymentInitiationResponse initiatePayment(Long userId, PaymentRequestDto paymentRequestDto) throws CommonsException {
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new CommonsException("user does  not exist", HttpStatus.BAD_REQUEST));

        Customer customer = new Customer();
        customer.setName(foundUser.getFirstName() + " " + foundUser.getLastName());
        customer.setEmail(foundUser.getEmail());
        customer.setPhoneNo(foundUser.getPhoneNumber());
        paymentRequestDto.setCustomer(customer);
        PaymentLogDto paymentLogDto = createPaymentLog(userId, paymentRequestDto);
        paymentRequestDto.setPaymentReference(paymentLogDto.getReference());
        paymentRequestDto.setTransactionReference(paymentLogDto.getTransactionId());
        paymentRequestDto.setCurrencyCode(PaymentCurrency.NGN);

        log.info("initiating payment [{}]", paymentLogDto);
        PaymentInitiationResponse response = flutterwaveService.generatePaymentLink(paymentRequestDto);
        log.info("payment details [{}]", response);
        response.setCurrency(paymentRequestDto.getCurrencyCode());
        updatePaymentLog(userId, paymentLogDto.getReference(), response);
        return response;

    }

    private void updatePaymentLog(Long userId, String reference, PaymentInitiationResponse response) throws CommonsException {
        long id = IAppendableReferenceUtils.getIdFrom(reference);
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new CommonsException("transaction does not exist", HttpStatus.NOT_FOUND));
        transaction.setProcessorReference(response.getProcessorReference());
        transaction.setProcessor(response.getProcessor().name());
        transaction.setStatus(response.getStatus());
        transaction.setPaymentLink(response.getPaymentLink());
        transaction.setPaymentCurrency(response.getCurrency());
        transaction.setTransactionReference(response.getPaymentReference());
        transactionRepository.save(transaction);
    }

    private PaymentLogDto createPaymentLog(Long userId, PaymentRequestDto paymentRequestDto) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(paymentRequestDto, transaction);
        transaction.setUserId(userId);
        transaction.setTransactionReference(paymentRequestDto.getTransactionReference());
        transaction.setPaymentCurrency(paymentRequestDto.getCurrencyCode());
        transaction.setStatus(PaymentStatus.NEW);

        transaction = transactionRepository.save(transaction);
        return PaymentLogDto.fromPaymentLog(transaction);
    }


    public PaymentLogDto getPayment(String reference) throws CommonsException {
        return getPayment(IAppendableReferenceUtils.getIdFrom(reference));
    }

    public PaymentLogDto getPayment(long id) throws CommonsException {

        Transaction cardPayments = transactionRepository.findById(id).orElseThrow(() -> new CommonsException("payment does not exist", HttpStatus.NOT_FOUND));
        //perform deep requery for pending payment
        if (cardPayments.getStatus() == PaymentStatus.PENDING) {
            cardPayments = requeryPayment(cardPayments);
        }
        return PaymentLogDto.fromPaymentLog(cardPayments);

    }

    private Transaction requeryPayment(Transaction payment) {
        try {
            FlutterwavePaymentRequeryResponseDto requeryResponseDto = flutterwaveService.getStatus(payment.getReference());
            //when status changes from processor, update payment log
            if (requeryResponseDto.data.status.equalsIgnoreCase("successful")) {
                payment.setProcessorReference(payment.getReference());
                payment.setStatus(PaymentStatus.SUCCESSFUL);
            } else if (requeryResponseDto.data.status.equalsIgnoreCase("failed")) {
                payment.setStatus(PaymentStatus.FAILED);

            }
            transactionRepository.save(payment);

            return payment;
        } catch (CommonsException ex) {
            log.error("requeryCardPayment reference:[{}] error: {}", payment.getReference(), ex.getMessage());
            return payment;
        }
    }

    public PageDto transactions(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findByUserId(userId, pageable);
        List<PaymentLogDto> paymentLogDtos = transactions.stream().map(PaymentLogDto::fromPaymentLog).collect(Collectors.toList());
        return PageDto.build(transactions, paymentLogDtos);
    }
}
