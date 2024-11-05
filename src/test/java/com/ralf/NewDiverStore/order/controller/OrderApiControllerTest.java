package com.ralf.NewDiverStore.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralf.NewDiverStore.order.domain.model.Order;
import com.ralf.NewDiverStore.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private Order order;

    private PageImpl<Order> page;


    @BeforeEach
    void setUp() {
        order = new Order();
        page = new PageImpl<>(List.of(order, new Order(), new Order()));
        when(orderService.createOrder(any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[0]);
        when(orderService.findOrders(any())).thenReturn(page);
        when(orderService.findOrder(order.getId())).thenReturn(order);
        when(orderService.updateOrder(any(), any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[1]);

    }

    @Test
    void shouldCreateOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(order)));
    }

    @Test
    void shouldGetOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/orders"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    void shouldGetOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/orders/{order-id}", order.getId()))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(order)));

    }

    @Test
    void shouldUpdateOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/api/v1/orders/{order-id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(order)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(order)));
    }

    @Test
    void shouldDeleteOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/v1/orders/{order-id}", order.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}