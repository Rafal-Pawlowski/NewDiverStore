package com.ralf.NewDiverStore.customer.service;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {


    public Customer createCustomer(Customer customer) {
    return null;
    }

    public Page<Customer> getCustomers(Pageable pageable) {
    return null;
    }

    public Customer getCustomer(UUID customerId) {
        return null;
    }

    public Customer updateCustomer(UUID customerId, Customer customerRequest) {
    return null;
    }

    public void deleteCustomer(UUID customerId) {
    }
}
