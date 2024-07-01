package com.ralf.NewDiverStore.customer.domain.model;

import com.ralf.NewDiverStore.order.domain.model.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private UUID id;

    private String name;
    private String email;
    private String address;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

}
