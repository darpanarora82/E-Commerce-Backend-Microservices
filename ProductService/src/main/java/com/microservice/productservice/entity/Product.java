package com.microservice.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private Double price;
    private long quantity;

    public Product(String productName, Double price) {
        this.productName = productName;
        this.price = price;
    }
}
