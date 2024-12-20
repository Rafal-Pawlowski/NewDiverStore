package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductApiController {


    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Page<Product> getProducts(Pageable pageable) {
        return productService.findAllProducts(pageable);
    }

    @GetMapping("{product-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Product getProduct(@PathVariable("product-id") UUID productId) {
        return productService.getSingleProduct(productId);
    }

    @PatchMapping("{product-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Product updateProduct(@PathVariable("product-id") UUID productId,
                                 @RequestBody Product product) {
        return productService.updateProduct(productId, product);
    }

    @DeleteMapping("{product-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("product-id") UUID productId) {
        productService.deleteProduct(productId);
    }


}
