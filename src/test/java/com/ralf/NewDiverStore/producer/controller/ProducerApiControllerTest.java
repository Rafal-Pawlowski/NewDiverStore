package com.ralf.NewDiverStore.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.service.ProducerService;
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
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProducerApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProducerService producerService;

    private Producer producer;

    private PageImpl<Producer> page;

    @BeforeEach
    void setUp() {
        producer = new Producer("Producer1");
        page = new PageImpl<>(List.of(producer, new Producer("Producer2"), new Producer("Producer3")));

        when(producerService.getProducers(any())).thenReturn(page);
        when(producerService.getSingleProducer(producer.getId())).thenReturn(producer);
        when(producerService.createProducer(any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[0]);
        when(producerService.updateProducer(any(), any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[1]);

    }

    @Test
    void shouldAddProducer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(producer)));
    }

    @Test
    void shouldGetProducers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/producers"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    void shouldGetProducer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/api/v1/producers/{producer-id}", producer.getId()))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(producer)));
    }

    @Test
    void shouldUpdateProducer() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .patch("http://localhost:8080/api/v1/producers/{producer-id}", producer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(producer)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(producer)));

    }

    @Test
    void shouldDeleteProducer() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/v1/producers/{id}", producer.getId()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}