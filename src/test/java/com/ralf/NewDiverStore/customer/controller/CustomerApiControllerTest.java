package com.ralf.NewDiverStore.customer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralf.NewDiverStore.customer.domain.model.Customer;
import com.ralf.NewDiverStore.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    private Customer customer;

    private PageImpl<Customer> page;

    @BeforeEach
    void setUp() {
        customer = new Customer("Name", "LastName", "email@email.com");
        page = new PageImpl<>(List.of(customer,
                new Customer("Adam", "Kowalski", "adam@kowalski.com"),
                new Customer("Marta", "Nowak", "marta@nowak.com")));

        when(customerService.createCustomer(any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[0]);
        when(customerService.getCustomers(any())).thenReturn(page);
        when(customerService.getCustomer(customer.getId())).thenReturn(customer);
        when(customerService.updateCustomer(any(), any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[1]);

    }

    @Test
    void shouldAddCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void shouldGetCustomers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/customers"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    void shouldGetCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/customers/{customer-id}", customer.getId()))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/api/v1/customers/{customer-id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(customer)));

    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/v1/customers/{customer-id}", customer.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}