package com.ralf.NewDiverStore.cart.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItem {

    @Id
    private UUID id;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    private int quantity;

    public CartItem(Cart cart, Product product, int quantity){
        this.cart= cart;
        this.product = product;
        this.quantity = quantity;
    }

}
