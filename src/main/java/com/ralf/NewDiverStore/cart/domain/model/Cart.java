package com.ralf.NewDiverStore.cart.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.shipping.model.Shipping;
import lombok.Getter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Getter
public class Cart {


    private List<CartItem> cartItems = new ArrayList<>();

    private int counter = 0;

    private BigDecimal sum = BigDecimal.ZERO;

    private Shipping shipping = new Shipping();

    private BigDecimal totalCost = BigDecimal.ZERO;



    public void addItem(Product product) {
        boolean notFound = true;

        for (CartItem ci : cartItems) {
            if (ci.getProduct().getId().equals(product.getId())) {
                notFound = false;
                ci.increaseCounter();
                recalculatePriceAndCounter();
                break;
            }
        }
        if (notFound) {
            cartItems.add(new CartItem(product));
            recalculatePriceAndCounter();
        }
    }

    public void removeItem(Product product) {
        for (CartItem ci : cartItems) {
            if (ci.getProduct().getId().equals(product.getId())) {
                ci.decreaseCounter();
                if (ci.hasZeroItems()) {
                    cartItems.remove(ci);
                }
                recalculatePriceAndCounter();
                break;
            }
        }
    }

    public void removeAllItems(Product product) {
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem ci = iterator.next();
            if (ci.getProduct().getId().equals(product.getId())) {
                iterator.remove();
            }
        }
        recalculatePriceAndCounter();
    }


    public void recalculatePriceAndCounter() {
        int tempCounter = 0;
        BigDecimal tempPrice = BigDecimal.ZERO;

        for (CartItem ci : cartItems) {
            tempCounter += ci.getCounter();
            tempPrice = tempPrice.add(ci.getPrice());
        }
        this.counter = tempCounter;
        this.sum = tempPrice;
    }

    public BigDecimal getTotalCost(){
        BigDecimal shippingCost = shipping.calculateShippingCost(sum);
        return sum.add(shippingCost);
    }


}
