package com.microservice.productservice.service;

import com.microservice.productservice.entity.Product;
import com.microservice.productservice.exception.ProductServiceCustomException;
import com.microservice.productservice.model.ProductRequest;
import com.microservice.productservice.model.ProductResponse;
import com.microservice.productservice.repository.ProductRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepo productRepo;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product..");
        Product product = Product.builder()
                .productName(productRequest.getProductName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        Long productId = productRepo.save(product).getProductId();
        log.info("product is saved!!");
        return productId;
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() ->
                new ProductServiceCustomException("product not found with id: " + id, "PRODUCT_NOT_FOUND")
        );
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        log.info("Product is found: {}", product);
        return productResponse;
    }

    @Override
    public void updateQuantityForProduct(Long id, Long quantity) {
        Product product = productRepo.findById(id).orElseThrow(() ->
                new ProductServiceCustomException("Could not find a product with id: " + id, "NOT_FOUND")
        );
        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Product quantity ordered is less than quantity present in inventory"
                    , "QUANTITY_NOT_AVAILABLE");
        }
        product.setQuantity(product.getQuantity() - quantity);
        Product savedProduct = productRepo.save(product);
        log.info("product is saved with id: {}", savedProduct.getProductId());
    }
}
