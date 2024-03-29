package com.paymentservice.paymentservice.service;

import com.paymentservice.paymentservice.model.PaymentRequest;
import com.paymentservice.paymentservice.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentById(Long id);
}
