package com.orderservice.orderservice.external.service;

import com.orderservice.orderservice.external.request.PaymentRequest;
import com.orderservice.orderservice.external.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE/payments")
public interface PaymentService {
    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);
}
