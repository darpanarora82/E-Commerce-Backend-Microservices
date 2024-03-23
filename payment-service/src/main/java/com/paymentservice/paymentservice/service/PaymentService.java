package com.paymentservice.paymentservice.service;

import com.paymentservice.paymentservice.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
