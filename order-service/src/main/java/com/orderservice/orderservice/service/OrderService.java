package com.orderservice.orderservice.service;

import com.orderservice.orderservice.model.OrderRequest;
import com.orderservice.orderservice.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse findProduct(Long id);
}
