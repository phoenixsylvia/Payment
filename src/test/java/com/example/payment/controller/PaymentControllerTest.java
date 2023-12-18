package com.example.payment.controller;

import com.example.payment.dto.CustomUser;
import com.example.payment.dto.PageDto;
import com.example.payment.enums.PaymentCurrency;
import com.example.payment.enums.PaymentStatus;
import com.example.payment.enums.Processor;
import com.example.payment.exceptions.CommonsException;
import com.example.payment.model.Transaction;
import com.example.payment.model.User;
import com.example.payment.payment.FlutterwaveService;
import com.example.payment.payment.PaymentInitiationResponse;
import com.example.payment.payment.PaymentLogDto;
import com.example.payment.payment.PaymentRequestDto;
import com.example.payment.repository.TransactionRepository;
import com.example.payment.repository.UserRepository;
import com.example.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentControllerTest {

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    private final FlutterwaveService flutterwaveService = mock(FlutterwaveService.class);

    private final UserRepository userRepository = mock(UserRepository.class);

    private final PaymentService paymentService = new PaymentService(flutterwaveService, userRepository, transactionRepository);

    private final PaymentController paymentController = new PaymentController(paymentService);
    private final Authentication authentication = mock(Authentication.class);

    @BeforeEach
    void setUp() {
        when(authentication.getPrincipal()).thenReturn(iUserDetails());
    }

    @Test
    void initiatePayment() throws CommonsException {
        User user = new User();
        user.setEmail("test@yahoo.com");
        user.setLastName("user");
        user.setFirstName("test");
        user.setPhoneNumber("123456789");
        user.setId(1L);
        Transaction transaction = transaction();
        PaymentInitiationResponse paymentInitiationResponse = new PaymentInitiationResponse();
        paymentInitiationResponse.setPaymentLink("paymentlink.com");
        paymentInitiationResponse.setStatus(PaymentStatus.PENDING);
        paymentInitiationResponse.setCurrency(PaymentCurrency.NGN);
        paymentInitiationResponse.setProcessor(Processor.FLUTTERWAVE);
        paymentInitiationResponse.setAmount(new BigDecimal(100));
        paymentInitiationResponse.setProcessorReference("1_qwerty");

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setRedirectUrl("google.com");
        paymentRequestDto.setAmount(new BigDecimal(100));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(transactionRepository.save(isA(Transaction.class))).thenReturn(transaction);
        when(flutterwaveService.generatePaymentLink(isA(PaymentRequestDto.class))).thenReturn(paymentInitiationResponse);
        when(transactionRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(transaction));
        transaction.setPaymentLink("paymentlink.com");
        transaction.setTransactionReference("1_qwerty");
        when(transactionRepository.save(isA(Transaction.class))).thenReturn(transaction);

        ResponseEntity<?> responseEntity = paymentController.initiatePayment(authentication, paymentRequestDto);
        PaymentInitiationResponse paymentInitiationResponse2 = (PaymentInitiationResponse) responseEntity.getBody();
        assertEquals(new BigDecimal(100), paymentInitiationResponse2.getAmount());
        assertEquals("paymentlink.com", paymentInitiationResponse2.getPaymentLink());
        assertEquals(PaymentStatus.PENDING, paymentInitiationResponse2.getStatus());
    }

    @Test
    void getTransactions() {
        List<Transaction> transactionList = Arrays.asList(transaction(), transaction());

        Page<Transaction> transactionPage = new PageImpl<>(transactionList);

        when(transactionRepository.findByUserId(anyLong(), isA(Pageable.class))).thenReturn(transactionPage);
        ResponseEntity<?> responseEntity = paymentController.getTransactions(authentication, "0", "3");
        PageDto transactions = (PageDto) responseEntity.getBody();
        PaymentLogDto transaction = (PaymentLogDto) transactions.getData().get(0);

        assertEquals(2, transactions.getTotalItems());
        assertEquals(1L, transaction.getUserId());
    }

    private CustomUser iUserDetails() {
        CustomUser iUserDetails = new CustomUser();
        iUserDetails.setId(1L);
        iUserDetails.setEmail("test@yahoo.com");
        return iUserDetails;
    }

    private Transaction transaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal(100));
        transaction.setPaymentCurrency(PaymentCurrency.NGN);
        transaction.setStatus(PaymentStatus.NEW);
        transaction.setUserId(1L);
        return transaction;
    }
}