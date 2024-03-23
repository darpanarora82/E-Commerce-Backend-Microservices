package com.paymentservice.paymentservice.service;

import com.paymentservice.paymentservice.entity.TransactionDetails;
import com.paymentservice.paymentservice.model.PaymentRequest;
import com.paymentservice.paymentservice.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording payment details: {}", paymentRequest);

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .orderId(paymentRequest.getOrderId())
                .paymentMode(paymentRequest.getPaymentMethod().name())
                .amount(paymentRequest.getAmount())
                .paymentDate(Instant.now())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .build();

        transactionDetailsRepository.save(transactionDetails);
        log.info("Transaction Details are saved: {}", transactionDetails.getId());
        return transactionDetails.getId();
    }
}
