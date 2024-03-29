package com.paymentservice.paymentservice.controller;

import com.paymentservice.paymentservice.model.PaymentRequest;
import com.paymentservice.paymentservice.model.PaymentResponse;
import com.paymentservice.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {
        long id = paymentService.doPayment(paymentRequest);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer') || hasAuthority('SCOPE_internal')")
    public ResponseEntity<PaymentResponse> getPaymentForOrder(@PathVariable("id") Long id) {
        PaymentResponse paymentResponse = paymentService.getPaymentById(id);
        return new ResponseEntity<>(paymentResponse, HttpStatus.FOUND);
    }
}
