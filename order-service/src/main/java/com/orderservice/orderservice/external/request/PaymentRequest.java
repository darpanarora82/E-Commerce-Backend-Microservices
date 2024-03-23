package com.orderservice.orderservice.external.request;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PaymentRequest {
    private Long orderId;
    private PaymentMethod paymentMethod;
    private String referenceNumber;
    private Instant paymentDate;
    private String paymentStatus;
    private Double amount;
}
