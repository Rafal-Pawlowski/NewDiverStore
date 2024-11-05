package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryAdminViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldDisplayIndexViewWithCategories() throws Exception {
        Page<Category> categoriesPage = new PageImpl<>(List.of(
                new Category("Category1"),
                new Category("Category2"),
                new Category("Category3")));

        when(categoryService.getCategories(any(), any())).thenReturn(categoriesPage);

        mockMvc.perform(get("/admin/categories")
                        .param("search", "")
                        .param("field", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/index"))
                .andExpect(model().attributeExists("categoriesPage", "search", "reverseSort", "field", "direction"));


    }

    @Test
    void singleView() {
    }

    @Test
    void editView() {
    }

    @Test
    void edit() {
    }

    @Test
    void deleteView() {
    }

    @Test
    void addView() {
    }

    @Test
    void add() {
    }
}