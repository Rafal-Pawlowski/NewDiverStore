package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.utilities.BreadcrumbItem;
import com.ralf.NewDiverStore.utilities.BreadcrumbsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private SessionCartService sessionCartService;

    @MockBean
    private BreadcrumbsService breadcrumbsService;


    @BeforeEach
    void setUp() {
        Cart mockCart = new Cart();
        Product product = new Product("Product", new BigDecimal("400.00"));
        mockCart.addItem(product);
        when(sessionCartService.getCart()).thenReturn(mockCart);
    }

    @Test
    void shouldDisplayIndexViewWithPopularCategories() throws Exception {

        Category drySuitCategory = new Category("Dry Suits");
        Category masksCategory = new Category("Masks");
        Category finsCategory = new Category("Fins");

        when(categoryService.getCategoryByName("Dry suits")).thenReturn(drySuitCategory);
        when(categoryService.getCategoryByName("Masks")).thenReturn(masksCategory);
        when(categoryService.getCategoryByName("Fins")).thenReturn(finsCategory);

        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/index"))
                .andExpect(model().attribute("categoryDrySuit", drySuitCategory))
                .andExpect(model().attribute("categoryMasks", masksCategory))
                .andExpect(model().attribute("categoryFins", finsCategory));
    }


    @Test
    void shouldDisplayCategoriesWithPaginationAndSorting() throws Exception {
        Category drySuitCategory = new Category("Dry Suits");
        Category masksCategory = new Category("Masks");
        Category finsCategory = new Category("Fins");

        Page<Category> categoriesPage = new PageImpl<>(List.of(drySuitCategory, masksCategory, finsCategory));
        when(categoryService.getCategories(any(Pageable.class))).thenReturn(categoriesPage);

        List<BreadcrumbItem> breadcrumbItems = new ArrayList<>();
        when(breadcrumbsService.breadcrumbsHomeCategories()).thenReturn(breadcrumbItems);

        mockMvc.perform(get("/categories")
                        .param("field", "name")
                        .param("direction", "asc")
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(view().name("category/list"))
                .andExpect(model().attribute("categoriesPage", categoriesPage))
                .andExpect(model().attribute("reverseSort", "desc"))
                .andExpect(model().attribute("field", "name"))
                .andExpect(model().attribute("direction", "asc"))
                .andExpect(model().attribute("breadcrumbs", breadcrumbItems))
                .andExpect(model().attributeExists("reverseSort", "field", "direction", "categoriesPage"));
    }
}
