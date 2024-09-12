package com.ralf.NewDiverStore.customer.domain.model;


import com.ralf.NewDiverStore.order.domain.model.Order;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "customers")
@NoArgsConstructor

public class Customer {

    @Id
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;

    @Embedded
    private Address shippingAddress;

    @Embedded
    private Address billingAddress;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;


    public Customer(UUID id, String firstName, String lastName, String email, Address shippingAddress, Address billingAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    public Customer addOrder(Order order) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        order.setCustomer(this);
        orders.add(order);

        return this;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

}

