package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.UUID;

@Controller
@RequestMapping("admin/categories")
public class CategoryAdminViewController {

    public final CategoryService categoryService;
    public final ProductService productService;

    public CategoryAdminViewController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String indexView(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "admin/category/index";
    }

    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model) {
        model.addAttribute("category", categoryService.getCategory(id));
        model.addAttribute("products", productService.findProductByCategoryId(id));
        model.addAttribute("producerCounter", productService.countProducersByCategoryId(id));
        return "admin/category/single";
    }

    @GetMapping("{id}/edit")
    public String editView(Model model, @PathVariable UUID id) {
        model.addAttribute("category", categoryService.getCategory(id));

        return "admin/category/edit";
    }


    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id) {
        categoryService.deleteCategory(id);

        return "redirect:/admin/categories";
    }


}
