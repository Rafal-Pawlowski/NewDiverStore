package com.ralf.NewDiverStore.order.domain.model;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {

    @Id
    private UUID id;

    private Payment payment;

    @ManyToOne
    private Customer customer;



    public Order(){
        this.id = UUID.randomUUID();
    }

    public Order(Payment payment) {
        this();
        this.payment = payment;
    }
}

