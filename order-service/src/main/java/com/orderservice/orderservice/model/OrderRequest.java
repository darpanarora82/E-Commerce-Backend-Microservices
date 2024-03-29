package com.orderservice.orderservice.model;

import com.orderservice.orderservice.external.request.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long productId;
    private Long quantity;
    private Double totalAmount;
    private PaymentMethod paymentMethod;
}
