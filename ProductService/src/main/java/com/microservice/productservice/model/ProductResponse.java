package com.microservice.productservice.model;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductResponse {
    public Long productId;
    public String productName;
    public Double price;
    public long quantity;
}
