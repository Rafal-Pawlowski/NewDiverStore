package com.ralf.NewDiverStore.product.service;

import com.ralf.NewDiverStore.product.domain.model.Product;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    public Product createProduct(Product product) {
        product.setId(UUID.randomUUID());
        return product;
    }

    public List<Product> findAllProducts() {
        List<Product> products = List.of(new Product("Diving mask", 200.00),
                new Product("Wetsuit", 500.00),
                new Product("Breathing device", 2040.00));

        return products;
    }

    public Product getSingleProduct(UUID id, UUID productId) {
        return new Product("Swimming pants" + id, 15.09);

    }

    public Product updateProduct(UUID id, UUID productId, Product productRequest) {
        Product product = new Product("Before update", 100.00);
        product.setName(productRequest.getName());
        return product;
    }

    public void deleteProduct(UUID id) {
    }
}
