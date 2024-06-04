package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Controller
@RequestMapping("admin/products")
public class ProductAdminViewController {


    public final ProductService productService;
    public final ProducerService producerService;
    public final CategoryService categoryService;


    public ProductAdminViewController(ProductService productService, ProducerService producerService, CategoryService categoryService) {
        this.productService = productService;
        this.producerService = producerService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String indexView(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "admin/product/index";
    }

    @GetMapping("add")
    public String addView(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product/add";
    }

    @PostMapping
    public String add(Product product) {
        productService.createProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model) {
        model.addAttribute("product", productService.getSingleProduct(id));

        return "admin/product/single";
    }

    @GetMapping("{id}/edit")
    public String editView(@PathVariable UUID id, Model model) {
        model.addAttribute("product", productService.getSingleProduct(id));
        model.addAttribute("producers", producerService.getProducers());
        model.addAttribute("categories", categoryService.getCategories());
        return "admin/product/edit";
    }

    @PostMapping("{id}/edit")
    public String edit(@ModelAttribute("product") Product product, @PathVariable UUID id, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            String imagePath = saveFile(file);
            product.setImagePath(imagePath);
        }
        productService.updateProduct(id, product);
        return "redirect:/admin/products/{id}";
    }

    private String saveFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.write(path, bytes);
            return fileName;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }


}
