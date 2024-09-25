package com.ralf.NewDiverStore.customer.domain.model;


import com.ralf.NewDiverStore.order.domain.model.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {

    @Id
    private UUID id;
    @NotBlank(message = "{divestore.validation.name.NotBlank.message}")
    @Size(min = 2, max = 255, message = "Size must be in range between 2 to 255 characters")
    private String firstName;


    @NotBlank(message = "{divestore.validation.name.NotBlank.message}")
    @Size(min = 2, max = 255, message = "Size must be in range between 2 to 255 characters")
    private String lastName;

    @NotBlank(message = "{divestore.validation.email.NotBlank.message}")
    @Size(min = 6, max = 255, message = "Size must be in range between 6 to 255 characters")
    private String email;


    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private ShippingAddress shippingAddress;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private BillingAddress billingAddress;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();


    public Customer() {
        this.id = UUID.randomUUID();
    }


    public Customer(String firstName, String lastName, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public Customer(String firstName, String lastName, String email, ShippingAddress shippingAddress, BillingAddress billingAddress) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;

    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }


    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(orders);
    }

    public void setId(UUID id) {
        this.id = id != null ? id : UUID.randomUUID();
    }


}

