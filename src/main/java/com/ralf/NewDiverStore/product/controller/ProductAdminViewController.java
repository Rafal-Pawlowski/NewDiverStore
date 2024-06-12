package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.producer.service.ProducerService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import com.ralf.NewDiverStore.utilities.ImageHandler;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
    public String indexView(Pageable pageable, Model model) {
        Page<Product> productsPage = productService.findAllProducts(pageable);
        model.addAttribute("productsPage", productsPage );
        paging(model,productsPage);
        return "admin/product/index";
    }

    @GetMapping("add")
    public String addView(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("producers", producerService.getProducers(Pageable.unpaged()));
        model.addAttribute("categories", categoryService.getCategories(Pageable.unpaged()));
        return "admin/product/add";
    }

    @PostMapping("add")
    public String add(
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes ra,
            Model model
            ) {
        if (!file.isEmpty()) {
            String imagePath = ImageHandler.saveFile(file);
            product.setImagePath(imagePath);
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("product", product);
            model.addAttribute("producers", producerService.getProducers(Pageable.unpaged()));
            model.addAttribute("categories", categoryService.getCategories(Pageable.unpaged()));
            return "admin/product/add";
        }

        try{
            productService.createProduct(product);
            ra.addFlashAttribute("message", Message.info("Product added"));
        }catch (Exception e) {
            model.addAttribute("product", product);
            model.addAttribute("message", Message.error("Unknown writing error"));
        }
        return "redirect:/admin/products";
    }

    @GetMapping("{id}/delete")
    public String deleteView(@PathVariable UUID id, RedirectAttributes ra) {

        try {
            productService.deleteProduct(id);
            ra.addFlashAttribute("message", Message.info("Product removed"));
        } catch (Exception e) {
            ra.addFlashAttribute("message", Message.error("Unknown error"));
        }
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
        model.addAttribute("producers", producerService.getProducers(Pageable.unpaged()));
        model.addAttribute("categories", categoryService.getCategories(Pageable.unpaged()));
        return "admin/product/edit";
    }

    @PostMapping("{id}/edit")
    public String edit(
            @PathVariable UUID id,
            @Valid @ModelAttribute("product") Product product,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes ra,
            Model model
    ) {
        Product existingProduct = productService.getSingleProduct(id);

        if (!file.isEmpty()) {
            String oldImagePath = existingProduct.getImagePath();
            String newImagePath = ImageHandler.saveFile(file);
            if (newImagePath != null) {
                product.setImagePath(newImagePath);
                ImageHandler.deleteFile(oldImagePath);
            }
        } else {
            product.setImagePath(existingProduct.getImagePath());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("producers", producerService.getProducers(Pageable.unpaged()));
            model.addAttribute("categories", categoryService.getCategories(Pageable.unpaged()));
            model.addAttribute("message", Message.error("Data writing error"));
            return "admin/product/edit";
        }

        try {
            productService.updateProduct(id, product);
            ra.addFlashAttribute("message", Message.info("Product updated"));
        } catch (Exception e) {
            model.addAttribute("product", product);
            model.addAttribute("message", Message.error("Unknown data writing error"));
        }

        return "redirect:/admin/products/{id}";
    }

    private void paging(Model model, Page page) {
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
    }


}































