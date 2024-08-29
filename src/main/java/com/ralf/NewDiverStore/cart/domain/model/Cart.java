package com.ralf.NewDiverStore.cart.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
public class Cart {


    private List<CartItem> items = new ArrayList<>();

    private int counter = 0;

    private BigDecimal sum = BigDecimal.ZERO;

    public void addItem(Product product){
        boolean notFound = true;

        for(CartItem ci: items){
            if(ci.getProduct().getId().equals(product.getId())){
                notFound = false;
                ci.increaseCounter();
                recalculatePriceAndCounter();
                break;
            }
        }
        if(notFound){
            items.add(new CartItem(product));
            recalculatePriceAndCounter();
        }
   }

   public void removeItem(Product product){
        for(CartItem ci: items){
            if(ci.getProduct().getId().equals(product.getId())){
                ci.decreaseCounter();
                if(ci.hasZeroItems()){
                    items.remove(ci);
                }
                recalculatePriceAndCounter();
                break;
            }
        }
   }

   private void recalculatePriceAndCounter(){
        int tempCounter = 0;
        Double tempPrice = 0.0;

        for(CartItem ci: items){
            tempCounter += ci.getCounter();
        }

   }



}
