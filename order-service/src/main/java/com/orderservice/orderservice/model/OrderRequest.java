package com.orderservice.orderservice.model;

import com.orderservice.orderservice.external.request.PaymentMethod;
import lombok.Data;

@Data
public class OrderRequest {
    private Long productId;
    private Long quantity;
    private Double totalAmount;
    private PaymentMethod paymentMethod;
}
