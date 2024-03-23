package com.orderservice.orderservice.entity;

import com.orderservice.orderservice.model.PaymentMethod;
import jakarta.persistence.*;
import jakarta.servlet.annotation.WebListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ORDER_DETAILS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long quantity;
    private Instant orderDate;
    private String orderStatus;
    private Double amount;
}
