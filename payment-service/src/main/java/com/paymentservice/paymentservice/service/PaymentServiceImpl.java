package com.paymentservice.paymentservice.service;

import com.paymentservice.paymentservice.entity.TransactionDetails;
import com.paymentservice.paymentservice.model.PaymentRequest;
import com.paymentservice.paymentservice.model.PaymentResponse;
import com.paymentservice.paymentservice.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

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
                .paymentMode(paymentRequest.getPaymentMethod())
                .amount(paymentRequest.getAmount())
                .paymentDate(Instant.now())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentStatus("COMPLETED")
                .build();

        transactionDetailsRepository.save(transactionDetails);
        log.info("Transaction Details are saved: {}", transactionDetails.getId());
        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        log.info("Finding the payment for the order id: {}", id);
        Optional<TransactionDetails> transactionDetails = transactionDetailsRepository.findByOrderId(id);
        if (transactionDetails.isPresent()) {
            TransactionDetails transaction = transactionDetails.get();
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .paymentMethod(transaction.getPaymentMode())
                    .amount(transaction.getAmount())
                    .paymentStatus(transaction.getPaymentStatus())
                    .paymentDate(transaction.getPaymentDate())
                    .build();

            log.info("Transaction details for order id: {} are {}", id, paymentResponse);

            return paymentResponse;
        }
        return null;
    }
}
