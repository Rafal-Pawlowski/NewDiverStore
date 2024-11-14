package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
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
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private SessionCartService sessionCartService;

    @MockBean
    private BreadcrumbsService breadcrumbsService;

    private List<BreadcrumbItem> breadcrumbItems;

    private List<Product> productsList;
    private Page<Product> productsPage;

    private Product product;

    private Category category;
    private Producer producer;

    @BeforeEach
    void setUp() {
        product = new Product("Product", new BigDecimal("345.00"));
        category = new Category("Category");
        producer = new Producer("Producer");
        product.setCategory(category);
        product.setProducer(producer);
        breadcrumbItems = new ArrayList<>();
        productsList = new ArrayList<>(List.of(product));
        productsPage = new PageImpl<>(List.of(product));

        Cart mockCart = new Cart();
        mockCart.addItem(product);
        when(sessionCartService.getCart()).thenReturn(mockCart);
        when(categoryService.getCategory(any(UUID.class))).thenReturn(category);
        when(productService.getSingleProduct(any())).thenReturn(product);
        when(breadcrumbsService.breadcrumbsHomeCategoriesCategoriesName(any(), any())).thenReturn(breadcrumbItems);
    }


    @Test
    void shouldDisplayTopProducts() throws Exception {
        when(breadcrumbsService.breadcrumbsHomeTop()).thenReturn(breadcrumbItems);
        when(productService.getTopProducts(anyInt())).thenReturn(productsList);

        mockMvc.perform(get("/top"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/top"))
                .andExpect(model().attribute("breadcrumbs", breadcrumbItems))
                .andExpect(model().attribute("topProducts", productsList));
    }

    @Test
    void shouldDisplayNewestProducts() throws Exception {

        when(breadcrumbsService.breadcrumbsHomeNewest()).thenReturn(breadcrumbItems);
        when(productService.getNewestProducts(anyInt(), any(Pageable.class))).thenReturn(productsPage);

        mockMvc.perform(get("/newest"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/newest"))
                .andExpect(model().attribute("breadcrumbs", breadcrumbItems))
                .andExpect(model().attribute("newestProductsPage", productsPage));

    }

    @Test
    void shouldDisplayProductsByCategoryView() throws Exception {


        when(productService.findProductByCategoryId(any(), any())).thenReturn(productsPage);


        mockMvc.perform(get("/categories/{category-id}/products", category.getId())
                        .param("field", "name")
                        .param("direction", "asc")
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().isOk())
                .andExpect(view().name("product/list"))
                .andExpect(model().attribute("breadcrumbs", breadcrumbItems))
                .andExpect(model().attribute("productsPage", productsPage))
                .andExpect(model().attribute("category", category))
                .andExpect(model().attributeExists("reverseSort", "field", "direction"));

        verify(categoryService).getCategory(category.getId());
        verify(breadcrumbsService).breadcrumbsHomeCategoriesCategoriesName(category.getName(), category.getId());
        verify(productService).findProductByCategoryId(any(UUID.class), any(Pageable.class));

    }

    @Test
    void shouldAddNewestProductToCart() throws Exception {
        // Given
        UUID productId = product.getId();
        when(productService.getSingleProduct(productId)).thenReturn(product);

        // When & Then
        mockMvc.perform(post("/product/add/{product-id}", productId)
                        .header("Referer", "/newest"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/newest"))
                .andExpect(flash().attribute("message", "Product has been added to cart!"));

        verify(sessionCartService, times(1)).addProductToCart(product);
    }

    @Test
    void shouldRedirectToNewestWhenReferrerContainsNewest() throws Exception {
        // Given
        UUID productId = product.getId();

        when(productService.getSingleProduct(productId)).thenReturn(product);

        // When & Then
        mockMvc.perform(post("/product/add/{product-id}", productId)
                        .header("Referer", "/newest"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/newest"));

        verify(sessionCartService, times(1)).addProductToCart(product);
    }

    @Test
    void shouldRedirectToTopWhenReferrerContainsTop() throws Exception {
        // Given
        UUID productId = product.getId();

        when(productService.getSingleProduct(productId)).thenReturn(product);

        // When & Then
        mockMvc.perform(post("/product/add/{product-id}", productId)
                        .header("Referer", "/top"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/top"));

        verify(sessionCartService, times(1)).addProductToCart(product);
    }

    @Test
    void shouldRedirectToHomeWhenReferrerIsUnknown() throws Exception {
        // Given
        UUID productId = product.getId();
        when(productService.getSingleProduct(productId)).thenReturn(product);

        // When & Then
        mockMvc.perform(post("/product/add/{product-id}", productId)
                        .header("Referer", "/unknown"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(sessionCartService, times(1)).addProductToCart(product);
    }


    @Test
    void shouldAddProductToCart() throws Exception {
        // Given
        UUID categoryId = category.getId();
        UUID productId = product.getId();

        when(productService.findProductByCategoryId(any(), any(Pageable.class))).thenReturn(productsPage);


        // When & Then
        mockMvc.perform(post("/categories/{category-id}/products/add/{product-id}", categoryId, productId)
                        .param("field", "name")
                        .param("direction", "asc")
                        .param("page", "0")
                        .param("size", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/" + categoryId + "/products"))
                .andExpect(flash().attribute("message", "Product has been added to cart!"));

        // Verify interactions
        verify(sessionCartService, times(1)).addProductToCart(product);
        verify(productService, times(1)).findProductByCategoryId(any(UUID.class), any(Pageable.class));
        verify(categoryService, times(1)).getCategory(categoryId);

    }

    @Test
    void shouldAddThreeProductsToCartInSingleView() throws Exception {

        mockMvc.perform(post("/add/{product-id}", product.getId())
                        .param("quantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/" + product.getId()))
                .andExpect(flash().attribute("message", Message.info("Product has been added to cart!")));

        verify(sessionCartService, times(3)).addProductToCart(product);
        verify(productService, times(1)).getSingleProduct(product.getId());
    }

    @Test
    void shouldDisplaySingleProduct() throws Exception {

        mockMvc.perform(get("/products/{product-id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("product/single"))
                .andExpect(model().attribute("product", product))
                .andExpect(model().attribute("breadcrumbs", breadcrumbItems));

        verify(productService).getSingleProduct(product.getId());
        verify(breadcrumbsService).breadcrumbsHomeCategoriesCategoriesName(any(),any());


    }
}