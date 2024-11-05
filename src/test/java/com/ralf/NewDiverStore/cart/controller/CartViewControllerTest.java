package com.ralf.NewDiverStore.cart.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.domain.model.ItemOperation;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.utilities.BreadcrumbsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionCartService sessionCartService;

    @MockBean
    private BreadcrumbsService breadcrumbsService;

    @BeforeEach
    void setUp() {
        when(sessionCartService.getCart()).thenReturn(new Cart());
        when(sessionCartService.getTotalCostShippingIncluded()).thenReturn(BigDecimal.valueOf(100));
        when(sessionCartService.getShippingCost()).thenReturn(BigDecimal.valueOf(10));
    }

    @Test
    void shouldDisplayCartViewIndex() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/index"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attributeExists("totalCostShippingIncluded"))
                .andExpect(model().attributeExists("shipping"))
                .andExpect(model().attribute("step", "cart"));

    }

    @Test
    void shouldUpdateCartItemQuantityAndRedirect() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(post("/cart/update/{productId}", productId)
                .param("action", "increase")
                .param("qty", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(sessionCartService).itemOperation(productId, ItemOperation.INCREASE);
    }

    @Test
    void shouldDeleteCartItemAndRedirect() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(get("/cart/delete/{productId}", productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(redirectedUrl("/cart"));

        verify(sessionCartService).itemOperation(productId, ItemOperation.REMOVE);
    }
}