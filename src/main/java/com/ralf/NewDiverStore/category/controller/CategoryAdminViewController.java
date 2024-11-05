package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import com.ralf.NewDiverStore.utilities.ImageHandler;
import com.ralf.NewDiverStore.utilities.Pagination;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ralf.NewDiverStore.utilities.Pagination.*;

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
    public String indexView(
            @RequestParam(name = "s", required = false, defaultValue = "") String search,
            @RequestParam(name = "field", required = false, defaultValue = "id") String field,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);

        String reverseSort = null;
        if ("asc".equals(direction)) {
            reverseSort = "desc";
        } else {
            reverseSort = "asc";
        }

        Page<Category> categoriesPage = categoryService.getCategories(search, pageable);
        model.addAttribute("categoriesPage", categoriesPage);
        model.addAttribute("search", search);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);


        paging(model, categoriesPage);

        return "admin/category/index";
    }

    @GetMapping("{id}")
    public String singleView(@PathVariable UUID id, Model model, Pageable pageable) {
        model.addAttribute("category", categoryService.getCategory(id));
        model.addAttribute("products", productService.findProductByCategoryId(id, pageable));
        model.addAttribute("producerCounter", productService.countProducersByCategoryId(id, pageable));
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
            @RequestParam("file") MultipartFile file,
            RedirectAttributes ra,
            Model model
    ) {
        Category existingCategory = categoryService.getCategory(id);

        if(!file.isEmpty()){
            String oldImagePath = existingCategory.getImagePath();
            String newImagePath = ImageHandler.saveFile(file);
            if(newImagePath !=null){
                category.setImagePath(newImagePath);
                ImageHandler.deleteFile(oldImagePath);
            }
        } else {
            category.setImagePath(existingCategory.getImagePath());
        }



        if (bindingResult.hasErrors()) {
            model.addAttribute("category", category);
            model.addAttribute("message", Message.error("Data writing error"));
            return "admin/category/edit";
        }

        try {
            categoryService.updateCategory(id, category);
            ra.addFlashAttribute("message", Message.info("Category updated"));

        } catch (Exception e) {
            model.addAttribute("category", category);
            model.addAttribute("message", Message.error("Unknown data writing error"));
            return "admin/category/edit";
        }
        return "redirect:/admin/categories";
    }


    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id, RedirectAttributes ra) {
        Page<Product> productList = productService.findProductByCategoryId(id, Pageable.unpaged());
        if (productList.isEmpty()) {
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
    public String add(
            @Valid @ModelAttribute("category") Category category,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes ra,
            Model model) {

        if (!file.isEmpty()) {
            String imagePath = ImageHandler.saveFile(file);
            category.setImagePath(imagePath);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("category", category);
            model.addAttribute("message", Message.error("Data writing error"));
            return "admin/category/add";
        }

        try {
            categoryService.createCategory(category);
            ra.addFlashAttribute("message", Message.info("New Category added"));
        } catch (Exception e) {
            model.addAttribute("category", category);
            model.addAttribute("message", Message.error("Unknown data writing error. Adding category failed."));
            return "admin/category/add";
        }

        return "redirect:/admin/categories";
    }




}
