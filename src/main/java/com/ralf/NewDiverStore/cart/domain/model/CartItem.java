package com.ralf.NewDiverStore.cart.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartItem {

    private Product product;

    private int counter;

    private BigDecimal price;

    public CartItem(Product product) {
        this.product = product;
        this.counter = 1;
        this.price = product.getDiscountedPrice();
    }

    public void increaseCounter() {
        counter++;
        recalculate();
    }

    public void decreaseCounter() {
        if (counter > 0) {
            counter--;
            recalculate();
        }
    }

    public boolean hasZeroItems() {
        return counter == 0;
    }

    private void recalculate() {
        price = product.getDiscountedPrice().multiply(new BigDecimal(counter));
    }

    public boolean idEquals(Product product) {
        return this.product.getId().equals(product.getId());
    }

}
