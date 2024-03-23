package com.orderservice.orderservice.controller;

import com.orderservice.orderservice.model.OrderRequest;
import com.orderservice.orderservice.model.OrderResponse;
import com.orderservice.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Log4j2
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {
        long id = orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrderById(@PathVariable("id") Long id) {
        OrderResponse orderResponse = orderService.findProduct(id);
        log.info("product found: {}", orderResponse);
        return new ResponseEntity<>(orderResponse, HttpStatus.FOUND);
    }
}
