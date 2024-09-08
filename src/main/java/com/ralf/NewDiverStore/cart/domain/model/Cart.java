package com.ralf.NewDiverStore.cart.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import lombok.Getter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
public class Cart {


    private List<CartItem> cartItems = new ArrayList<>();

    private int counter = 0;

    private BigDecimal sum = BigDecimal.ZERO;



    public void addItem(Product product){
        boolean notFound = true;

        for(CartItem ci: cartItems){
            if(ci.getProduct().getId().equals(product.getId())){
                notFound = false;
                ci.increaseCounter();
                recalculatePriceAndCounter();
                break;
            }
        }
        if(notFound){
            cartItems.add(new CartItem(product));
            recalculatePriceAndCounter();
        }
   }

   public void removeItem(Product product){
        for(CartItem ci: cartItems){
            if(ci.getProduct().getId().equals(product.getId())){
                ci.decreaseCounter();
                if(ci.hasZeroItems()){
                    cartItems.remove(ci);
                }
                recalculatePriceAndCounter();
                break;
            }
        }
   }


   private void recalculatePriceAndCounter(){
        int tempCounter = 0;
        BigDecimal tempPrice = BigDecimal.ZERO;

        for(CartItem ci: cartItems){
            tempCounter += ci.getCounter();
            tempPrice = tempPrice.add(ci.getPrice());
        }
        this.counter = tempCounter;
        this.sum=tempPrice;

   }





}
