package com.ralf.NewDiverStore.order.domain.model;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    private BigDecimal totalOrderPrice;
    private BigDecimal shippingCost;


    public Order() {
        this.id = UUID.randomUUID();
        this.orderTime = LocalDateTime.now();
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }




}

