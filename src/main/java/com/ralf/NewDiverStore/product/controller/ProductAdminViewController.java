package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.producer.service.ProducerService;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ralf.NewDiverStore.utilities.Pagination.*;


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

        Page<Product> productsPage = productService.findAllProducts(search, pageable);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("search", search);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseSort", reverseSort);
        paging(model, productsPage);
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

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("producers", producerService.getProducers(Pageable.unpaged()));
            model.addAttribute("categories", categoryService.getCategories(Pageable.unpaged()));
            return "admin/product/add";
        }

        try {
            productService.createProduct(product);
            ra.addFlashAttribute("message", Message.info("Product added"));
        } catch (Exception e) {
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

}































