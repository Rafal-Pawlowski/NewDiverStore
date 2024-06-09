package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
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

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable UUID id,
            @Valid @ModelAttribute("category") Category category,
            BindingResult bindingResult,
            RedirectAttributes ra,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", category);
            model.addAttribute("message", Message.error("Data writing error"));
            return "admin/category/edit";
        }

        try {
            categoryService.updateCategory(id, category);
            ra.addFlashAttribute("message", Message.info("Category updated"));

        }catch (Exception e){
            model.addAttribute("category", category);
            model.addAttribute("message", Message.error( "Unknown data writing error"));
            return "admin/category/edit";
        }
        return "redirect:/admin/categories";
    }


    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id, RedirectAttributes ra) {
        List<Product>productList = productService.findProductByCategoryId(id);
        if(productList.isEmpty()){
            categoryService.deleteCategory(id);
            ra.addFlashAttribute("message", Message.info("Category removed"));
        } else {
            ra.addFlashAttribute("message", Message.error("Category cannot be removed. Please delete related products first"));
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("add")
    public String addView(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/add";
    }

    @PostMapping("add")
    public String add(@ModelAttribute("category") Category category) {
        categoryService.createCategory(category);
        return "redirect:/admin/categories";
    }


}
