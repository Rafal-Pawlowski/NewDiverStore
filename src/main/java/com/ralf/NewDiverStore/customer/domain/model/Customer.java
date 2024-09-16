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
@NoArgsConstructor
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


    @OneToOne
    private ShippingAddress shippingAddress;

    @OneToOne
    private BillingAddress billingAddress;

    private boolean sameAddress;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;




    public Customer(UUID id, String firstName, String lastName, String email, ShippingAddress shippingAddress, BillingAddress billingAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    public Customer addOrder(Order order) {
        if (orders == null) {
            orders = new HashSet<>();
        }
        order.setCustomer(this);
        orders.add(order);

        return this;
    }

    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(orders);
    }

}

