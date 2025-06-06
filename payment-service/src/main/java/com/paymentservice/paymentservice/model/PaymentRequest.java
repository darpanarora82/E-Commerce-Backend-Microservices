package com.paymentservice.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private PaymentMethod paymentMethod;
    private String referenceNumber;
    private Instant paymentDate;
    private String paymentStatus;
    private Double amount;
}
