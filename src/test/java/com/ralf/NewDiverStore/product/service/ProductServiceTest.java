package com.ralf.NewDiverStore.product.service;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.domain.repository.CategoryRepository;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.domain.repository.ProducerRepository;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        producerRepository.deleteAll();

    }

    @Test
    void shouldCreateProduct() {
        //given
        Category category = categoryRepository.save(new Category("Category"));
        Producer producer = producerRepository.save(new Producer("Producer"));

        Product productRequest = new Product("productRequest", new BigDecimal("150.00"));
        productRequest.setDescription("productRequest description text");
        productRequest.setProducer(producer);
        productRequest.setCategory(category);
        productRequest.setImagePath("image/path");

        //when
        Product result = productService.createProduct(productRequest);

        //then
        assertThat(productRepository.existsById(result.getId())).isTrue();
        assertThat(result)
                .extracting(Product::getName, Product::getPrice , Product::getDescription, Product::getProducer, Product::getCategory, Product::getImagePath)
                .containsExactly(productRequest.getName(), productRequest.getPrice() , productRequest.getDescription(), productRequest.getProducer(), productRequest.getCategory(), productRequest.getImagePath());

        assertThat(category.getProducts())
                .extracting(Product::getId)
                .containsExactly(result.getId());

        assertThat(producer.getProducts())
                .extracting(Product::getId)
                .containsExactly(result.getId());

    }

    @Test
    void findAllProducts() {
    }

    @Test
    void testFindAllProducts() {
    }

    @Test
    void getSingleProduct() {
    }

    @Test
    void getTopProducts() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void findProductByCategoryId() {
    }

    @Test
    void countProducersByCategoryId() {
    }

    @Test
    void getProductsByProducerId() {
    }

    @Test
    void getNewestProducts() {
    }
}