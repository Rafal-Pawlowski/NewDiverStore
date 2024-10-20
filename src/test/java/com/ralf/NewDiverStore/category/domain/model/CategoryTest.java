package com.ralf.NewDiverStore.category.domain.model;

import com.ralf.NewDiverStore.category.domain.repository.CategoryRepository;
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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class CategoryTest {

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category("Category");
        product = new Product("Product", new BigDecimal("100.00"));
    }

    @Test
    void shouldAddProductToEmptyListAndSetCategory() {
        //given - setUp
        //when
        category.addProduct(product);

        //then
        assertThat(category.getProducts().size()).isEqualTo(1);
        assertThat(product.getCategory().getName()).isEqualTo(category.getName());

    }

    @Test
    void shouldAddProductToExistingListAndSetCategory() {
        //given
        LinkedHashSet<Product> products = new LinkedHashSet<>();
        Product existingProduct = new Product("Product", new BigDecimal("100.00"));
        products.add(existingProduct);
        Category category = new Category("Category");
        category.setProducts(products);

        Product newProduct  = new Product("AnotherProduct", new BigDecimal(120.00));

        //when
        category.addProduct(newProduct);

        //then
        assertThat(category.getProducts().size()).isEqualTo(2);
        assertThat(newProduct.getCategory().getName()).isEqualTo(category.getName());
        assertThat(category.getProducts()).contains(existingProduct, newProduct);

    }

    @Test
    void shouldNotAddDuplicateProduct() {
        // given - setUp

        // when
        category.addProduct(product);
        category.addProduct(product);

        // then
        assertThat(category.getProducts().size()).isEqualTo(1);
        assertThat(product.getCategory()).isEqualTo(category);

    }

}