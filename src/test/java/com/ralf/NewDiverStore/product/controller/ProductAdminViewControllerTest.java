package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import com.ralf.NewDiverStore.utilities.ImageHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductAdminViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProducerService producerService;

    @MockBean
    private CategoryService categoryService;

    private Page<Product> productsPage;

    private Page<Category> categoriesPage;
    private Page<Producer> producersPage;

    private Product product;
    private Category category;
    private Producer producer;

    @BeforeEach
    void setUp() {
        product = new Product("Product", new BigDecimal("100.00"));
        category = new Category("Category");
        producer = new Producer("Producer");
        product.setCategory(category);
        product.setProducer(producer);
        productsPage = new PageImpl<>(List.of(product));
        categoriesPage = new PageImpl<>(List.of(category));
        producersPage = new PageImpl<>(List.of(producer));

        when(producerService.getProducers(any())).thenReturn(producersPage);
        when(categoryService.getCategories(any())).thenReturn(categoriesPage);
        when(productService.getSingleProduct(any())).thenReturn(product);
        when(productService.updateProduct(any(), any())).thenAnswer((InvocationOnMock invocationOnMock) -> invocationOnMock.getArguments()[1]);

    }


    @Test
    void shouldDisplayIndexView() throws Exception {

        when(productService.findAllProducts(any(), any())).thenReturn(productsPage);

        mockMvc.perform(get("/admin/products")
                        .param("search", "")
                        .param("field", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/index"))
                .andExpect(model().attribute("productsPage", productsPage))
                .andExpect(model().attributeExists("search", "field", "direction", "reverseSort"));

        verify(productService).findAllProducts(any(), any());
    }

    @Test
    void shouldDisplayAddView() throws Exception {

        mockMvc.perform(get("/admin/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/add"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("producers", producersPage))
                .andExpect(model().attribute("categories", categoriesPage));
    }

    @Test
    void shouldProcessAddProductAndRedirectToProducts() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[0]);
        String expectedImagePath = "path/to/image.jpg";


        try (MockedStatic<ImageHandler> mockedImageHandler = mockStatic(ImageHandler.class)) {
            mockedImageHandler.when(() -> ImageHandler.saveFile(mockFile)).thenReturn(expectedImagePath);

            mockMvc.perform(multipart("/admin/products/add")
                            .file(mockFile)
                            .flashAttr("product", product))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/products"))
                    .andExpect(flash().attribute("message", Message.info("Product added")));

            verify(productService).createProduct(product);
        }
    }

    @Test
    void shouldReturnAddViewWhenAddingInvalidProduct() throws Exception {
        Product invalidProduct = new Product();

        mockMvc.perform(multipart("/admin/products/add")
                        .file("file", new byte[0])
                        .flashAttr("product", invalidProduct))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/add"))
                .andExpect(model().attribute("product", invalidProduct))
                .andExpect(model().attribute("producers", producersPage))
                .andExpect(model().attribute("categories", categoriesPage));
        verify(productService, never()).createProduct(invalidProduct);
    }

    @Test
    void shouldDeleteProductAndRedirectToProducts() throws Exception {
        UUID id = product.getId();

        mockMvc.perform(get("/admin/products/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"))
                .andExpect(flash().attribute("message", Message.info("Product removed")));
        verify(productService).deleteProduct(id);

    }

    @Test
    void shouldDisplaySingleProduct() throws Exception {
        UUID productId = product.getId();
        product.setImagePath("local/path");
        mockMvc.perform(get("/admin/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/single"))
                .andExpect(model().attribute("product", product));
        verify(productService).getSingleProduct(productId);

    }

    @Test
    void shouldDisplayEditView() throws Exception {
        UUID productId = product.getId();

        mockMvc.perform(get("/admin/products/{id}/edit", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/edit"))
                .andExpect(model().attribute("product", product))
                .andExpect(model().attribute("producers", producersPage))
                .andExpect(model().attribute("categories", categoriesPage));

        verify(productService).getSingleProduct(productId);
        verify(producerService).getProducers(Pageable.unpaged());
        verify(categoryService).getCategories(Pageable.unpaged());

    }

    @Test
    void shouldProcessProductEditionAndRedirectToSingleProduct() throws Exception {
        UUID productId = product.getId();

        MockMultipartFile mockFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[0]);
        String expectedImagePath = "path/to/image.jpg";


        try (MockedStatic<ImageHandler> mockedImageHandler = mockStatic(ImageHandler.class)) {
            mockedImageHandler.when(() -> ImageHandler.saveFile(mockFile)).thenReturn(expectedImagePath);

            mockMvc.perform(multipart("/admin/products/{id}/edit", productId)
                            .file(mockFile)
                            .flashAttr("product", product))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/products/" + productId))
                    .andExpect(flash().attribute("message", Message.info("Product updated")));

            verify(productService).updateProduct(productId, product);
        }
    }
}