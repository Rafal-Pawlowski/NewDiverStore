package com.ralf.NewDiverStore.customer.domain.repository;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
