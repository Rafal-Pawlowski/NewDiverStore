package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;


@Controller
@RequestMapping("admin/products")
public class ProductAdminViewController {


    public final ProductService productService;


    public ProductAdminViewController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String indexView(Model model){
        model.addAttribute("products", productService.findAllProducts());
        return "admin/product/index";
    }

    @GetMapping("add")
    public String addView(Model model){
        model.addAttribute("product", new Product());
        return "admin/product/add";
    }

    @PostMapping
    public String add(Product product){
        productService.createProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id){
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

}
