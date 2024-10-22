package com.ralf.NewDiverStore.customer.service;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.domain.model.CustomerBuilder;
import com.ralf.NewDiverStore.customer.domain.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
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
                .build();

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer createOrUpdateCustomerWithNamesAndEmail(Customer customerRequest) {
        logger.debug("CreateOrUpdateCustomer method");
        if (customerRequest.getId() != null) {
            Optional<Customer> existingCustomer = customerRepository.findById(customerRequest.getId());
            if (existingCustomer.isPresent()) {
                Customer customer = existingCustomer.get();
                customer.setFirstName(customerRequest.getFirstName());
                customer.setLastName(customerRequest.getLastName());
                customer.setEmail(customerRequest.getEmail());
                logger.debug("Before Saving customer in createOrUpdateCustomer method: {}", customer);
                return customerRepository.save(customer);
            }
        }
        logger.debug("Before Saving customerRequest in createOrUpdateCustomer method: {}", customerRequest);
        return createCustomer(customerRequest);
    }


    @Transactional(readOnly = true)
    public Page<Customer> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(UUID customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            return customer;

        } else {
            throw new EntityNotFoundException("Customer with id: " + customerId + " not found");
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
