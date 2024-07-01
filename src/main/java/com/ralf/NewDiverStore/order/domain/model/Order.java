package com.ralf.NewDiverStore.order.domain.model;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.orderItem.domain.model.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private UUID id;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    private Date orderDate;

    private String status;


}
