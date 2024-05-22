package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Controller
@RequestMapping("admin/products")
public class ProductAdminViewController {


    public final ProductService productService;
    public final ProducerService producerService;


    public ProductAdminViewController(ProductService productService, ProducerService producerService) {
        this.productService = productService;
        this.producerService = producerService;
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

    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model){
        model.addAttribute("product", productService.getSingleProduct(id));

        return "admin/product/single";
    }

    @GetMapping("{id}/edit")
    public String editView(@PathVariable UUID id, Model model){
        model.addAttribute("product", productService.getSingleProduct(id));
        model.addAttribute("producers", producerService.getProducers());
        return "admin/product/edit";
    }

    @PostMapping("{id}/edit")
    public String edit(@ModelAttribute("product") Product product, @PathVariable UUID id){
        productService.updateProduct(id, product);
        return "redirect:/admin/products/{id}";
    }

}
