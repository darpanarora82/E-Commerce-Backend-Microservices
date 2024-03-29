package com.microservice.productservice.service;

import com.microservice.productservice.entity.Product;
import com.microservice.productservice.exception.ProductServiceCustomException;
import com.microservice.productservice.model.ProductRequest;
import com.microservice.productservice.model.ProductResponse;
import com.microservice.productservice.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {
    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    ProductService productService = new ProductServiceImpl();

    @Test
    @DisplayName("Test Product added to database - Success Scenario")
    void test_Product_Add_To_Database() {
        //mock
        Product product = mockProduct();
        ProductRequest productRequest = mockProductRequest();

        when(productRepo.save(any(Product.class))).thenReturn(product);
        //call
        long id = productService.addProduct(productRequest);

        //verify
        verify(productRepo, times(1)).save(any());

        //assert
        assertEquals(id, product.getProductId());
        assertEquals(productService.addProduct(productRequest), product.getProductId());
    }

    @Test
    @DisplayName("Test Get Product By Id - Success Scenario")
    void test_Get_Product_By_Id() {
        //mock
        Product product = mockProduct();
        ProductResponse productResponse = mockProductResponse();
        when(productRepo.findById(anyLong())).thenReturn(Optional.of(product));

        //call
        ProductResponse productById = productService.getProductById(product.getProductId());

        //verify
        verify(productRepo, times(1)).findById(anyLong());

        //assert
        assertEquals(product.getProductId(), productById.productId);
        assertEquals(productResponse, productById);
    }

    @Test
    @DisplayName("Test Get Product By Id - Failure Scenario")
    void test_Get_Product_By_Id_Failure() {
        //mock
        when(productRepo.findById(anyLong())).thenReturn(Optional.empty());

        //call

        //verify

        //assert
        ProductServiceCustomException exception = assertThrows(ProductServiceCustomException.class, () -> productService.getProductById(1L));

        assertEquals(exception.getErrorCode(), "PRODUCT_NOT_FOUND");

    }

    @Test
    @DisplayName("Test Updating Quantity For Product - Success Scenario")
    void test_Updating_Quantity_For_Product() {
        //mock
        Product product = mockProduct();
        long initialQuantity = product.getQuantity();
        when(productRepo.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepo.save(any(Product.class))).thenReturn(product);

        //call
        productService.updateQuantityForProduct(1L, 1L);

        //verify
        verify(productRepo, times(1)).findById(1L);
        verify(productRepo, times(1)).save(any(Product.class));

        //assert
        assertEquals(initialQuantity - 1L, product.getQuantity());
    }

    @Test
    @DisplayName("Test Updating Quantity For Product - Failure where cant find product Scenario")
    void test_Failure_Finding_Product() {
        //mock
        when(productRepo.findById(anyLong())).thenReturn(Optional.empty());

        //call

        //verify

        //assert
        ProductServiceCustomException exception = assertThrows(ProductServiceCustomException.class, () -> productService.updateQuantityForProduct(1L, 1L));

        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(exception.getMessage(), "Could not find a product with id: " + 1L);
    }

    private ProductResponse mockProductResponse() {
        return ProductResponse.builder()
                .productName("iPad")
                .price(1000.0)
                .productId(1L)
                .quantity(10)
                .build();
    }

    private ProductRequest mockProductRequest() {
        return ProductRequest.builder()
                .productName("iPad")
                .price(1000.0)
                .quantity(10L)
                .build();
    }

    private Product mockProduct() {
        return Product.builder()
                .productName("iPad")
                .price(1000.0)
                .productId(1L)
                .quantity(10)
                .build();
    }

}