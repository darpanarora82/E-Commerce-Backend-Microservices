package com.orderservice.orderservice.service;

import com.orderservice.orderservice.entity.Order;
import com.orderservice.orderservice.exception.CustomException;
import com.orderservice.orderservice.external.request.PaymentRequest;
import com.orderservice.orderservice.external.service.PaymentService;
import com.orderservice.orderservice.external.service.ProductService;
import com.orderservice.orderservice.model.OrderRequest;
import com.orderservice.orderservice.model.OrderResponse;
import com.orderservice.orderservice.repository.OrderRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Order is getting placed!!");

        productService.updateProductQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("creating order with CREATED order status!!");
        Order order = Order.builder()
                .orderDate(Instant.now())
                .productId(orderRequest.getProductId())
                .amount(orderRequest.getTotalAmount())
                .quantity(orderRequest.getQuantity())
                .orderStatus("CREATED")
                .build();
        Order savedOrder = orderRepo.save(order);
        log.info("Order is placed with Order ID: {}", savedOrder.getId());
        log.info("Payment is now getting processed");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(savedOrder.getId())
                .amount(savedOrder.getAmount())
                .paymentDate(Instant.now())
                .paymentMethod(orderRequest.getPaymentMethod())
                .build();
        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment success. Order Placed");
            orderStatus = "ORDER_PLACED";
        } catch (Exception e) {
            log.info("Payment Failed. Order Cancelled");
            orderStatus = "FAILED";
        }
        savedOrder.setOrderStatus(orderStatus);
        orderRepo.save(savedOrder);
        return savedOrder.getId();
    }

    @Override
    public OrderResponse findProduct(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new CustomException("Product not found with id: " + id,
                "PRODUCT_NOT_FOUND", 404));
        log.info("Product is found with id: " + id);


        log.info("Invoking product service to get product details for product id: {}", order.getProductId());
        OrderResponse.ProductDetails productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + order.getProductId(), OrderResponse.ProductDetails.class);


        log.info("Invoking payment service to get payment details for product id: {}", order.getProductId());
        OrderResponse.PaymentDetails paymentDetails = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payments/" + order.getId(), OrderResponse.PaymentDetails.class);

        return OrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .orderId(order.getId())
                .productDetails(productResponse)
                .paymentDetails(paymentDetails)
                .build();
    }
}
