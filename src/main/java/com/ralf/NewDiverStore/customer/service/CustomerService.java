package com.ralf.NewDiverStore.customer.service;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.model.CustomerBuilder;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer createCustomer(Customer customerRequest) {
        Customer customer = new CustomerBuilder()
                .withFirstName(customerRequest.getFirstName())
                .withLastName(customerRequest.getLastName())
                .withEmail(customerRequest.getEmail())
                .withShippingAddress(customerRequest.getShippingAddress())
                .withBillingAddress(customerRequest.getBillingAddress())
                .build();

    return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Page<Customer> getCustomers(Pageable pageable) {
    return customerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(UUID customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            return customer;

        } else{
            throw  new EntityNotFoundException("Customer with id: " + customerId + " not found");
        }
    }
    @Transactional
    public Customer updateCustomer(UUID customerId, Customer customerRequest) {
        Customer customer = getCustomer(customerId);
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setEmail(customerRequest.getEmail());
        customer.setShippingAddress(customerRequest.getShippingAddress());
        customer.setBillingAddress(customerRequest.getBillingAddress());

    return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(UUID customerId) {
        customerRepository.deleteById(customerId);
    }
}
