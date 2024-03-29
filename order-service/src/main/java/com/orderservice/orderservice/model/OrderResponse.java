package com.orderservice.orderservice.model;

import com.orderservice.orderservice.external.response.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Instant orderDate;
    private String orderStatus;
    private Double amount;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDetails {
        public Long productId;
        public String productName;
        public Double price;
        public long quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetails {
        private PaymentMethod paymentMethod;
        private Instant paymentDate;
        private String paymentStatus;
        private Double amount;
    }
}
