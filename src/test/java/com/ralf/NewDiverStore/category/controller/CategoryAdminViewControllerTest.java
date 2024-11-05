package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    void singleView() throws Exception {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category("Category1");
        Product product1 = new Product("Product1", new BigDecimal("100.00"));
        product1.setProducer(new Producer("Producer1"));
        Product product2 = new Product("Product2", new BigDecimal("50.00"));
        product2.setProducer(new Producer("Producer2"));
        Page<Product> productsPage = new PageImpl<>(List.of(product1, product2));
        when(categoryService.getCategory(categoryId)).thenReturn(category);
        when(productService.findProductByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(productsPage);
        when(productService.countProducersByCategoryId(any(), any())).thenReturn(1L);

        mockMvc.perform(get("/admin/categories/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/single"))
                .andExpect(model().attributeExists("category", "products", "producerCounter"));

    }

    @Test
    void shouldReturnEditCategoryView() throws Exception {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category("Category");
        when(categoryService.getCategory(categoryId)).thenReturn(category);

        mockMvc.perform(get("/admin/categories/{id}/edit", categoryId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/edit"))
                .andExpect(model().attribute("category", category));

    }

    @Test
    void shouldEditCategoryAndRedirect() throws Exception {
        UUID categoryId = UUID.randomUUID();
        Category category = new Category("Category");
        when(categoryService.getCategory(categoryId)).thenReturn(category);
        when(categoryService.updateCategory(any(), any())).thenAnswer((InvocationOnMock invocationOnMock)-> invocationOnMock.getArguments()[1]);

        mockMvc.perform(multipart("/admin/categories/"+categoryId + "/edit")
                .file("file", new byte[0])
                .flashAttr("category", category))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    void shouldDeleteCaetgoryAndRedirect() throws Exception {
        UUID categoryId = UUID.randomUUID();
        Page<Product> emptyProductsPage = new PageImpl<>(Collections.emptyList());
        when(productService.findProductByCategoryId(categoryId, Pageable.unpaged())).thenReturn(emptyProductsPage);

        mockMvc.perform(get("/admin/categories/"+categoryId+"/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/admin/categories"));

    }

    @Test
    void shouldReturnAddCategoryView() throws Exception {
        mockMvc.perform(get("/admin/categories/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/category/add"))
                .andExpect(model().attributeExists("category"));

    }
    @Test
    void shouldAddCategoryAndRedirect() throws Exception {
        // Given
        Category category = new Category("New Category");
        MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[0]);

        when(categoryService.createCategory(any())).thenReturn(category);

        // When & Then
        mockMvc.perform(multipart("/admin/categories/add")
                        .file(file)
                        .flashAttr("category", category))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/admin/categories"));
    }

}