package com.ralf.NewDiverStore.product.service;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.domain.repository.CategoryRepository;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.domain.repository.ProducerRepository;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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

    @Autowired
    private RedisProductService redisProductService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        producerRepository.deleteAll();
    }

    @AfterEach
    void clearRedis() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    private Product createProduct(String name, BigDecimal price, Category category, Producer producer) {
        Product product = new Product(name, price);
        product.setCategory(category);
        product.setProducer(producer);
        return productRepository.save(product);
    }

    private Category createCategory(String name) {
        return categoryRepository.save(new Category(name));
    }

    private Producer createProducer(String name) {
        return producerRepository.save(new Producer(name));
    }


    @Test
    void shouldCreateProduct() {
        //given
        Category category = createCategory("Category");
        Producer producer = createProducer("Producer");

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
                .extracting(Product::getName, Product::getPrice, Product::getDescription, Product::getProducer, Product::getCategory, Product::getImagePath)
                .containsExactly(productRequest.getName(), productRequest.getPrice(), productRequest.getDescription(), productRequest.getProducer(), productRequest.getCategory(), productRequest.getImagePath());

        assertThat(category.getProducts())
                .extracting(Product::getId)
                .containsExactly(result.getId());

        assertThat(producer.getProducts())
                .extracting(Product::getId)
                .containsExactly(result.getId());

    }

    @Test
    void shouldFindAllProducts() {
        //given
        productRepository.saveAll(List.of(
                new Product("Product1", new BigDecimal("123")),
                new Product("Product2", new BigDecimal("210")),
                new Product("Product3", new BigDecimal("343.00"))
        ));

        //when
        Page<Product> result = productService.findAllProducts(Pageable.unpaged());

        //then
        assertThat(result).hasSize(3)
                .extracting(Product::getName)
                .containsExactly("Product1", "Product2", "Product3");

    }

    @Test
    void shouldFindOneProductWithQuery() {
        //given

        String searchQuery = "oneTwoThree";

        productRepository.saveAll(List.of(
                new Product("Product1", new BigDecimal("123")),
                new Product("Prod" + searchQuery + "uct2", new BigDecimal("210")),
                new Product("Product3", new BigDecimal("343.00"))
        ));

        //when
        Page<Product> result = productService.findAllProducts(searchQuery, Pageable.unpaged());

        //then
        assertThat(result)
                .hasSize(1)
                .extracting(Product::getPrice)
                .containsExactly(new BigDecimal("210"));


    }

    @Test
    void shouldFindMoreProductsWithQuery() {
        //given

        String searchQuery = "abc";

        productRepository.saveAll(List.of(
                new Product("Product1", new BigDecimal("123")),
                new Product("Prod" + searchQuery + "uct2", new BigDecimal("210")),
                new Product("Product3" + searchQuery, new BigDecimal("343"))
        ));

        //when
        Page<Product> result = productService.findAllProducts(searchQuery, Pageable.unpaged());

        //then
        assertThat(result)
                .hasSize(2)
                .extracting(Product::getPrice)
                .containsExactly(new BigDecimal("210"), new BigDecimal("343"));
    }


    @Test
    void shouldGetSingleProduct() {
        //given
        Product product = new Product("ProductTest", new BigDecimal("100"));
        Product savedProduct = productRepository.save(product);

        //when
        Product result = productService.getSingleProduct(savedProduct.getId());

        //then
        assertThat(result)
                .isNotNull()
                .extracting(Product::getId, Product::getName)
                .containsExactly(savedProduct.getId(), savedProduct.getName());

    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileGettingSingleProduct() {
        //given
        UUID id = UUID.randomUUID();

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.getSingleProduct(id);
        });
        assertThat(exception.getMessage()).isEqualTo("Product with id: " + id + " not found");
    }

    @Test
    void shouldGetTopProducts() {

        // given
        Product product1 = productRepository.save(new Product("Product 1", new BigDecimal("100.00")));
        Product product2 = productRepository.save(new Product("Product 2", new BigDecimal("150.00")));
        Product product3 = productRepository.save(new Product("Product 3", new BigDecimal("200.00")));

        // Ustawienie liczby zamówień za pomocą RedisProductService
        redisProductService.setOrderCount(product1.getId(), 10L);
        redisProductService.setOrderCount(product2.getId(), 5L);
        redisProductService.setOrderCount(product3.getId(), 15L);

        // when
        List<Product> topProducts = productService.getTopProducts(2);

        // then
        assertThat(topProducts).hasSize(2);
        assertThat(topProducts.get(0).getId()).isEqualTo(product3.getId());
        assertThat(topProducts.get(1).getId()).isEqualTo(product1.getId());
    }


    @Test
    void shouldUpdateProduct() {
        //given
        Product product = productRepository.save(new Product("Product", new BigDecimal("100")));

        Product productRequest = new Product("UpdatedProduct", new BigDecimal("50"));
        productRequest.setDescription("shortDescriptionToUpdate");
        Producer producer = createProducer("Producer");
        productRequest.setProducer(producer);
        Category category = createCategory("Category");
        productRequest.setCategory(category);
        productRequest.setImagePath("image/path");
        productRequest.setDiscountPercentage(new BigDecimal("5"));

        //where
        Product result = productService.updateProduct(product.getId(), productRequest);

        //then
        assertThat(result)
                .isNotNull()
                .extracting(Product::getId, Product::getName, Product::getPrice, Product::getDescription,
                        Product::getProducer, Product::getCategory, Product::getImagePath, Product::getDiscountPercentage)
                .containsExactly(product.getId(), productRequest.getName(), productRequest.getPrice(), productRequest.getDescription(),
                        productRequest.getProducer(), productRequest.getCategory(), productRequest.getImagePath(), productRequest.getDiscountPercentage());

        assertThat(category.getProducts())
                .extracting(Product::getId)
                .containsExactly(result.getId());

        assertThat(producer.getProducts())
                .extracting(Product::getId)
                .containsExactly(result.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhileUpdatingProduct() {
        //given
        UUID id = UUID.randomUUID();
        Product productRequest = new Product();

        //when & then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> productService.updateProduct(id, productRequest));

        assertThat(exception.getMessage()).isEqualTo("Product with id: " + id + " not found");

    }

    @Test
    void shouldDeleteProduct() {
        //given
        Product product = productRepository.save(new Product("ProductName", new BigDecimal("120")));
        UUID id = product.getId();

        //when
        Throwable throwable = catchThrowable(()->productService.deleteProduct(id));

        //then
        assertThat(throwable).isNull();
        assertThat(productRepository.findById(id)).isEmpty();

    }

    @Test
    void findProductByCategoryId() {
        //given
        Category category1 = createCategory("Category1");
        Category category2 = createCategory("Category2");
        Producer producer = createProducer("Producer");

        Product product1 = createProduct("Product1", new BigDecimal("99"), category1, producer);
        Product product2 = createProduct("Product2", new BigDecimal("123"), category2, producer);
        Product product3 = createProduct("Product3", new BigDecimal("400"), category2, producer);


        //when
        Page<Product> result = productService.findProductByCategoryId(category2.getId(), Pageable.unpaged());

        //then
        assertThat(result)
                .hasSize(2)
                .extracting(Product::getName)
                .containsExactly(product2.getName(), product3.getName());

    }

    @Test
    void countProducersByCategoryId() {
        //given
        Category category1 = createCategory("Category1");
        Category category2 = createCategory("Category2");
        Producer producer1 = createProducer("Producer1");
        Producer producer2 = createProducer("Producer2");
        Product product1 = createProduct("Product1", new BigDecimal("99"), category1, producer1);
        Product product2 =createProduct("Product2", new BigDecimal("123"), category2, producer2);
        Product product3 = createProduct("Product3", new BigDecimal("400"), category2, producer1);

        //when
        long result1 = productService.countProducersByCategoryId(category2.getId(), Pageable.unpaged());
        long result2 = productService.countProducersByCategoryId(category1.getId(), Pageable.unpaged());

        //then
        assertThat(result1).isEqualTo(2);
        assertThat(result2).isEqualTo(1);

    }

    @Test
    void getProductsByProducerId() {
        //given
        Category category = createCategory("Category");
        Producer producer1 = createProducer("Producer");
        Producer producer2 = createProducer("Producer2");
        Product product1 = createProduct("Product1", new BigDecimal("99"), category, producer1);
        Product product2 = createProduct("Product2", new BigDecimal("123"), category, producer2);
        Product product3 = createProduct("Product3", new BigDecimal("400"), category, producer1);

        //when
        List<Product> result = productService.getProductsByProducerId(producer1.getId());

        //then
        assertThat(result).hasSize(2)
                .extracting(Product::getId)
                .containsExactly(product1.getId(), product3.getId());

    }

    @Test
    void getNewestProducts() {
        //given
        Category category = createCategory("Category");
        Producer producer = createProducer("Producer");

        Product product1 = createProduct("Product1", new BigDecimal("99"), category, producer);
        Product product2 = createProduct("Product2", new BigDecimal("400"), category, producer);
        Product product3 = createProduct("Product3", new BigDecimal("500"), category, producer);
        Product product4 = createProduct("Product4", new BigDecimal("600"), category, producer);


        //when
        Page<Product> result = productService.getNewestProducts(3, Pageable.unpaged());

        //then
        assertThat(result).hasSize(3)
                .extracting(Product::getId)
                .containsExactly(product4.getId(), product3.getId(), product2.getId());

    }
}