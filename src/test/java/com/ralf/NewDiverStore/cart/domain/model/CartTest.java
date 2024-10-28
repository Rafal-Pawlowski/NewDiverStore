package com.ralf.NewDiverStore.cart.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp(){
        cart = new Cart();
    }

    @Test
    void shouldAddItemToCart() {
        // given
        Product product = new Product("Product1", new BigDecimal("100.00"));
        product.setId(UUID.randomUUID());

        // when
        cart.addItem(product);

        // then
        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getProduct()).isEqualTo(product);
        assertThat(cart.getSum()).isEqualTo(new BigDecimal("100.00"));
        assertThat(cart.getCounter()).isEqualTo(1);
    }

    @Test
    void shouldIncreaseCounterWhenAddingSameItem() {
        // given
        Product product = new Product("Product1", new BigDecimal("100.00"));
        product.setId(UUID.randomUUID());

        // when
        cart.addItem(product);
        cart.addItem(product);

        // then
        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getCounter()).isEqualTo(2);
        assertThat(cart.getSum()).isEqualTo(new BigDecimal("200.00"));
        assertThat(cart.getCounter()).isEqualTo(2);
    }

    @Test
    void shouldRemoveSingleItemFromCart() {
        // given
        Product product = new Product("Product1", new BigDecimal("100.00"));
        product.setId(UUID.randomUUID());
        cart.addItem(product);
        cart.addItem(product);

        // when
        cart.removeItem(product);

        // then
        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getCounter()).isEqualTo(1);
        assertThat(cart.getSum()).isEqualTo(new BigDecimal("100.00"));
        assertThat(cart.getCounter()).isEqualTo(1);
    }
    @Test
    void shouldRemoveItemCompletelyWhenCounterReachesZero() {
        // given
        Product product = new Product("Product1", new BigDecimal("100.00"));
        product.setId(UUID.randomUUID());
        cart.addItem(product);

        // when
        cart.removeItem(product);

        // then
        assertThat(cart.getCartItems()).isEmpty();
        assertThat(cart.getSum()).isEqualTo(BigDecimal.ZERO);
        assertThat(cart.getCounter()).isEqualTo(0);
    }

    @Test
    void shouldRemoveAllItemsOfProduct() {
        // given
        Product product1 = new Product("Product1", new BigDecimal("100.00"));
        product1.setId(UUID.randomUUID());
        Product product2 = new Product("Product2", new BigDecimal("50.00"));
        product2.setId(UUID.randomUUID());
        cart.addItem(product1);
        cart.addItem(product1);
        cart.addItem(product2);

        // when
        cart.removeAllItems(product1);

        // then
        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getProduct()).isEqualTo(product2);
        assertThat(cart.getSum()).isEqualTo(new BigDecimal("50.00"));
        assertThat(cart.getCounter()).isEqualTo(1);
    }

    @Test
    void shouldCalculateTotalCostIncludingShipping() {
        // given
        Product product = new Product("Product1", new BigDecimal("100.00"));
        product.setId(UUID.randomUUID());
        cart.addItem(product);

        // when
        BigDecimal totalCost = cart.getTotalCost();

        // then
        assertThat(totalCost).isEqualTo(new BigDecimal("120.00"));
    }

    @Test
    void shouldRecalculatePriceAndCounter() {
        // given
        Product product1 = new Product("Product1", new BigDecimal("100.00"));
        product1.setId(UUID.randomUUID());
        Product product2 = new Product("Product2", new BigDecimal("50.00"));
        product2.setId(UUID.randomUUID());

        // when
        cart.addItem(product1);
        cart.addItem(product2);
        cart.recalculatePriceAndCounter();

        // then
        assertThat(cart.getSum()).isEqualTo(new BigDecimal("150.00"));
        assertThat(cart.getCounter()).isEqualTo(2);
    }
}