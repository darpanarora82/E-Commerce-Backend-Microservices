package com.microservice.productservice.service;

import com.microservice.productservice.model.ProductRequest;
import com.microservice.productservice.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long id);

    void updateQuantityForProduct(Long id, Long quantity);
}
