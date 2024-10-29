package com.ralf.NewDiverStore.category.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private PageImpl<Category> page;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Category1");
        page = new PageImpl<>(List.of(category, new Category("Cat2"), new Category("Cat3")));

        when(categoryService.getCategories(any())).thenReturn(page);
        when(categoryService.getCategory(category.getId())).thenReturn(category);
        when(categoryService.createCategory(any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[0]);
        when(categoryService.updateCategory(any(), any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[1]);

    }

    @Test
    void shouldGetCategories() throws Exception {
        mockMvc.perform(get("http://localhost:8080/api/v1/categories"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(page)));

    }

    @Test
    void shouldGetCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/categories/{categoryId}", category.getId()))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(category)));
    }


    @Test
    void shouldAddCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("http://localhost:8080/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(category)));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/api/v1/categories/{category-id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(category)));

    }

    @Test
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/v1/categories/{category-id}", category.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}