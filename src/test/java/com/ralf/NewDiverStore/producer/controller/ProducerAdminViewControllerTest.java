package com.ralf.NewDiverStore.producer.controller;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProducerAdminViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerService producerService;

    @MockBean
    private ProductService productService;

    private PageImpl<Producer> producersPage;

    private Producer producer;

    @BeforeEach
    void setUp() {
        producer = new Producer("Producer");
        producersPage = new PageImpl<>(List.of(producer, new Producer("Producer2"), new Producer("Producer3")));


    }

    @Test
    void shouldDisplayIndexView() throws Exception {

        when(producerService.getProducers(any(), any())).thenReturn(producersPage);

        mockMvc.perform(get("/admin/producers")
                        .param("s", "")
                        .param("field", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/producer/index"))
                .andExpect(model().attribute("producersPage", producersPage))
                .andExpect(model().attributeExists("search", "field", "direction", "reverseSort"));
    }

    @Test
    void shouldDeleteProducerWhenProductListIsEmptyAndRedirectToProducersView() throws Exception {
        List<Product> productsList = new ArrayList<>();
        UUID id = producer.getId();

        when(productService.getProductsByProducerId(id)).thenReturn(productsList);

        mockMvc.perform(get("/admin/producers/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/producers"))
                .andExpect(flash().attributeExists("message"));

        verify(producerService).deleteProducer(id);
    }

    @Test
    void shouldNotDeleteProducerWhenProductListIsNotEmptyAndRedirectToProducersWithErrorMessage() throws Exception {
        List<Product> productsList = List.of(new Product(), new Product());
        UUID id = producer.getId();

        when(productService.getProductsByProducerId(id)).thenReturn(productsList);

        mockMvc.perform(get("/admin/producers/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/producers"))
                .andExpect(flash().attributeExists("message"));

        verify(producerService, never()).deleteProducer(id);
    }

    @Test
    void shouldDisplayEditView() throws Exception {
        UUID id = producer.getId();

        when(producerService.getSingleProducer(id)).thenReturn(producer);

        mockMvc.perform(get("/admin/producers/{id}/edit", id))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/producer/edit"))
                .andExpect(model().attribute("producer", producer));
    }

    @Test
    void shouldProcessEditAndRedirectToProducers() throws Exception {
        UUID id = producer.getId();
        when(producerService.updateProducer(id, producer)).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[1]);

        mockMvc.perform(post("/admin/producers/{id}/edit", id)
                        .flashAttr("producer", producer))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/admin/producers"));

        verify(producerService).updateProducer(id, producer);
    }

    @Test
    void shouldDisplaySingleView() throws Exception {
        Category category = new Category("Category");
        Product product = new Product();
        product.setCategory(category);
        List<Product> productsList = List.of(product);

        when(producerService.getSingleProducer(any())).thenReturn(producer);
        when(productService.getProductsByProducerId(any())).thenReturn(productsList);

        mockMvc.perform(get("/admin/producers/{id}", producer.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/producer/single"))
                .andExpect(model().attribute("producer", producer))
                .andExpect(model().attribute("products", productsList));

    }

    @Test
    void shouldDisplayAddView() throws Exception {
        mockMvc.perform(get("/admin/producers/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/producer/add"))
                .andExpect(model().attributeExists("producer"));
    }

    @Test
    void shouldProcessAddingProducerAndRedirectToProducers() throws Exception {

        mockMvc.perform(post("/admin/producers/add")
                        .flashAttr("producer", producer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/producers"))
                .andExpect(flash().attributeExists("message"));

        verify(producerService).createProducer(producer);
    }

    @Test
    void shouldNotValidateProducerAndReturnAddingProducerForm() throws Exception {
        Producer invalidProducer = new Producer();

        mockMvc.perform(post("/admin/producers/add")
                .flashAttr("producer", invalidProducer))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/producer/add"))
                .andExpect(model().attribute("producer", invalidProducer))
                .andExpect(model().attribute("message", Message.error("Producer adding error")));

        verify(producerService, never()).createProducer(invalidProducer);
    }
}