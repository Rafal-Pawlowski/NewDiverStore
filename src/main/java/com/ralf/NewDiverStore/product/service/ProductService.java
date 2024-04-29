package com.ralf.NewDiverStore.product.service;

import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(Product productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getSingleProduct(UUID id, UUID productId) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return product;
        } else {
            throw new EntityNotFoundException("Product with id: " + id + " not found");
        }
    }

    @Transactional
    public Product updateProduct(UUID producerId, UUID productId, Product productRequest) {
        Product product = new Product("Before update", 100.00);
        product.setName(productRequest.getName());
        return product;
    }

    @Transactional
    public void deleteProduct(UUID id) {
    }
}
