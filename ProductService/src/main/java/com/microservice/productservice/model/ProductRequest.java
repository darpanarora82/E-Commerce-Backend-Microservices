package com.microservice.productservice.model;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private Double price;
    private Long quantity;
}
