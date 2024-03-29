package com.orderservice.orderservice.external.response;

import com.orderservice.orderservice.external.request.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private PaymentMethod paymentMethod;
    private Instant paymentDate;
    private String paymentStatus;
    private Double amount;
}
