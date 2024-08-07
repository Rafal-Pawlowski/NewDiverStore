package com.ralf.NewDiverStore.product.service;

import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;
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
        product.setDescription(productRequest.getDescription());
        product.setProducer(productRequest.getProducer());
        productRequest.getProducer().addProduct(product);
        product.setCategory(productRequest.getCategory());
        productRequest.getCategory().addProduct(product);
        product.setImagePath(productRequest.getImagePath());

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> findAllProducts(Pageable pageable) {
        return findAllProducts(null, pageable);
    }
    @Transactional(readOnly = true)
    public Page<Product> findAllProducts(String search, Pageable pageable) {
        if(search==null){
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findByNameContainingIgnoreCase(search, pageable);
        }
    }

    @Transactional(readOnly = true)
    public Product getSingleProduct(UUID id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            return product;
        } else {
            throw new EntityNotFoundException("Product with id: " + id + " not found");
        }
    }

    @Transactional
    public Product updateProduct(UUID productId, Product productRequest) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setDescription(productRequest.getDescription());
            product.setProducer(productRequest.getProducer());
            productRequest.getProducer().addProduct(product);
            product.setCategory(productRequest.getCategory());
            productRequest.getCategory().addProduct(product);
            product.setImagePath(productRequest.getImagePath());

            return productRepository.save(product);
        } else {
            throw new EntityNotFoundException("Product with id: " + productId + " not found");
        }
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Product> findProductByCategoryId(UUID categoryId, Pageable pageable) {
        return productRepository.findProductByCategoryId(categoryId, pageable);
    }

    @Transactional(readOnly = true)
    public long countProducersByCategoryId(UUID id, Pageable pageable) {
        Page<Product> products = findProductByCategoryId(id, pageable);
        long producerNumber = products.stream()
                .map(Product::getProducer)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        return producerNumber;
    }


    @Transactional(readOnly = true)
    public List<Product> getProductsByProducerId(UUID id) {
        return productRepository.findProductByProducerId(id);
    }
}
