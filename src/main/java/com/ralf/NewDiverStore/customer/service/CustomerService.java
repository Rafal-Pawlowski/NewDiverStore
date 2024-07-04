package com.ralf.NewDiverStore.customer.service;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Transactional
    public Customer createCustomer(Customer customer) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<Customer> getCustomers(Pageable pageable) {
        return null;
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(UUID id) {
        return null;
    }

    @Transactional
    public Customer updateCustomer(UUID id, Customer customer) {
        return null;
    }


    @Transactional
    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }
}
