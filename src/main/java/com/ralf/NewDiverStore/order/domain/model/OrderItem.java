package com.ralf.NewDiverStore.order.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="order_items")
@Getter
@Setter
@NoArgsConstructor

public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private BigDecimal pricePerOne;
    private BigDecimal totalPrice;
    public OrderItem(Order order, Product product, int quantity, BigDecimal pricePerOne) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.pricePerOne = pricePerOne;
        this.totalPrice = pricePerOne.multiply(BigDecimal.valueOf(quantity));
    }

}
