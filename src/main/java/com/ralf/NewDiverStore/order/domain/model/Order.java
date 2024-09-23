package com.ralf.NewDiverStore.order.domain.model;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Order {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Payment payment;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDateTime orderTime;


    public Order() {
        this.id = UUID.randomUUID();
        this.orderTime = LocalDateTime.now();
    }

//    public Order(Payment payment) {
//        this();
//        this.payment = payment;
//    }


}

