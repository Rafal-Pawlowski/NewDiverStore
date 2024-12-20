package com.ralf.NewDiverStore.customer.controller;

import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerApiController {


    private final CustomerService customerService;

    public CustomerApiController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer addCustomer(@RequestBody Customer customerRequest){
        return customerService.createCustomer(customerRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Page<Customer> getCustomers(Pageable pageable){
        return customerService.getCustomers(pageable);
    }

    @GetMapping("{customer-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Customer getCustomer(@PathVariable("customer-id")UUID customerId){
        return customerService.getCustomer(customerId);
    }

    @PutMapping("{customer-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer updateCustomer(@PathVariable("customer-id")UUID customerId, @RequestBody Customer customerRequest){
        return customerService.updateCustomer(customerId, customerRequest);
    }

    @DeleteMapping("{customer-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("customer-id")UUID customerId){
        customerService.deleteCustomer(customerId);
    }

}
