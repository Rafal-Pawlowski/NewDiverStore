package com.ralf.NewDiverStore.cart.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();


    public void addItem(Product product, int quantity){
        for(CartItem item : items){
            if (item.getProduct().getId().equals(product.getId())){
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(this, product, quantity));
    }

    public void updateItem(Product product, int quantity){
        for(CartItem item : items){
            if(item.getProduct().getId().equals(product.getId())){
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public void removeItem(Product product){
        items.removeIf(item -> item.getProduct().getId().equals(product.getId()));
    }

    public double getTotal(){
        return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

}
