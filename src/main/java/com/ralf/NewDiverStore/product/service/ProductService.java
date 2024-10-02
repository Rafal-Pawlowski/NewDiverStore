package com.ralf.NewDiverStore.product.service;

import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {


    private final ProductRepository productRepository;

    private final RedisTemplate<String, String> redisTemplate;

    public ProductService(ProductRepository productRepository, RedisTemplate<String, String> redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
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
        if (search == null) {
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

    @Transactional(readOnly = true)
    public List<Product> getTopProducts(int topN) {
        List<Product> topProducts = new ArrayList<>();

        List<Product> allProducts = productRepository.findAll();

        Map<Product, Long> productOrderCountMap = new HashMap<>();
        for (Product product : allProducts) {
            String redisKey = "product:order_count:" + product.getId().toString();
            String orderCount = redisTemplate.opsForValue().get(redisKey);

            if (orderCount != null) {
                productOrderCountMap.put(product, Long.valueOf(orderCount));
            }
        }
        //sortowanie produktów na podstawie liczby zamówien
        productOrderCountMap.entrySet().stream()
                .sorted(Map.Entry.<Product, Long>comparingByValue().reversed())
                .limit(topN)
                .forEach(entry -> topProducts.add(entry.getKey()));

        return topProducts;
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
            product.setDiscountPercentage(productRequest.getDiscountPercentage());

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

    @Transactional(readOnly = true)
    public Page<Product> getNewestProducts(int limit, Pageable pageable) {

        List<Product> allProducts = productRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        List<Product> limitedProducts = allProducts.stream()
                .limit(limit)
                .toList();

        int start = Math.min((int) pageable.getOffset(), limitedProducts.size());
        int end = Math.min((start + pageable.getPageSize()), limitedProducts.size());

        return new PageImpl<>(limitedProducts.subList(start, end), pageable, limitedProducts.size());
    }
}