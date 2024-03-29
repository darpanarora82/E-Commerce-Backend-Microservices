package com.orderservice.orderservice.service;

import com.orderservice.orderservice.entity.Order;
import com.orderservice.orderservice.exception.CustomException;
import com.orderservice.orderservice.external.request.PaymentRequest;
import com.orderservice.orderservice.external.service.PaymentService;
import com.orderservice.orderservice.external.service.ProductService;
import com.orderservice.orderservice.model.OrderRequest;
import com.orderservice.orderservice.model.OrderResponse;
import com.orderservice.orderservice.model.PaymentMethod;
import com.orderservice.orderservice.repository.OrderRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static com.orderservice.orderservice.external.request.PaymentMethod.*;
import static com.orderservice.orderservice.model.OrderResponse.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {
    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();


    @Test
    @DisplayName("Find Product Method - Success Scenario")
    void test_Find_Product_Success_Scenario() {
        //Mock
        Order order = mockOrder();
        ProductDetails productResponse = mockProductResponse();
        PaymentDetails paymentResponse = mockPaymentResponse();

        when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/products/" + order.getProductId(), ProductDetails.class)).thenReturn(productResponse);

        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payments/" + order.getId(), PaymentDetails.class)).thenReturn(paymentResponse);

        //Actual
        OrderResponse orderResponse = orderService.findProduct(1L);

        //Verification
        verify(orderRepo, times(1)).findById(anyLong());

        verify(restTemplate, times(1))
                .getForObject("http://PRODUCT-SERVICE/products/" + order.getProductId(), ProductDetails.class);

        verify(restTemplate, times(1))
                .getForObject("http://PAYMENT-SERVICE/payments/" + order.getId(), PaymentDetails.class);


        //Assertion
        assertNotNull(orderResponse);
        assertEquals(orderResponse.getOrderId(), order.getId());
    }

    @Test
    @DisplayName("Find Product Method - Failure Scenario")
    void test_Find_Product_Failure_Scenario() {
        when(orderRepo.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> orderService.findProduct(1L));

        assertEquals(exception.status, 404);
        assertEquals(exception.errorCode, "PRODUCT_NOT_FOUND");
    }

    @Test
    @DisplayName("Test Place Order - Success Scenario")
    void test_Place_Order_Success_Scenario() {
        //mock
        OrderRequest orderRequest = mockOrderRequest();
        Order order = mockOrder();
        PaymentRequest paymentRequest = mockPaymentRequest();

        when(productService.updateProductQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));


        when(orderRepo.save(any(Order.class))).thenReturn(order);

        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L, HttpStatus.OK));

        //call
        long orderId = orderService.placeOrder(orderRequest);

        //verification
        verify(orderRepo, times(2)).save(any());
        verify(productService, times(1)).updateProductQuantity(anyLong(), anyLong());
        verify(paymentService, times(1)).doPayment(any());

        //assertion
        assertEquals(orderId, order.getId());
    }


    @Test
    @DisplayName("Test Place Order - Failure Scenario")
    void test_Place_Order_Failure_Scenario() {
        //mock
        Order order = mockOrder();
        PaymentRequest paymentRequest = mockPaymentRequest();
        OrderRequest orderRequest = mockOrderRequest();

        when(orderRepo.save(any())).thenReturn(order);
        when(productService.updateProductQuantity(anyLong(), anyLong())).thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(paymentRequest)).thenThrow(new RuntimeException());

        //call
        long id = orderService.placeOrder(orderRequest);

        //verify
        verify(orderRepo, times(2)).save(any());
        verify(productService, times(1)).updateProductQuantity(anyLong(), anyLong());
        verify(paymentService, times(1)).doPayment(any());

        //assert
        assertEquals(order.getId(), id);
    }

    private PaymentRequest mockPaymentRequest() {
        return PaymentRequest.builder()
                .orderId(1L)
                .referenceNumber("123")
                .paymentMethod(COD)
                .paymentDate(Instant.now())
                .amount(1000.0)
                .build();
    }

    private OrderRequest mockOrderRequest() {
        return OrderRequest.builder()
                .paymentMethod(CREDIT_CARD)
                .productId(1L)
                .totalAmount(1000.0)
                .quantity(1L)
                .build();
    }

    private PaymentDetails mockPaymentResponse() {
        return PaymentDetails.builder()
                .paymentMethod(PaymentMethod.COD)
                .paymentStatus("COMPLETED")
                .paymentDate(Instant.now())
                .amount(1000.0)
                .build();
    }

    private ProductDetails mockProductResponse() {
        return ProductDetails.builder()
                .price(100.0)
                .productId(1L)
                .productName("iPhone")
                .quantity(100)
                .build();
    }

    private Order mockOrder() {
        return Order.builder()
                .orderStatus("COMPLETED")
                .orderDate(Instant.now())
                .quantity(1L)
                .amount(1000.0)
                .id(1L)
                .productId(1L)
                .build();
    }

}