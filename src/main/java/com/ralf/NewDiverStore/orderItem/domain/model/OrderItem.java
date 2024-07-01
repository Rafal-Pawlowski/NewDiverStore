package com.ralf.NewDiverStore.orderItem.domain.model;

import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "orderItems")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    private UUID id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private int quantity;

    private double price;



}
