package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import com.ralf.NewDiverStore.utilities.BreadcrumbItem;
import com.ralf.NewDiverStore.utilities.BreadcrumbsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductViewController {


    private final ProductService productService;

    private final CategoryService categoryService;

    private final SessionCartService sessionCartService;

    private final BreadcrumbsService breadcrumbsService;


    public ProductViewController(ProductService productService, CategoryService categoryService, SessionCartService sessionCartService, BreadcrumbsService breadcrumbsService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.sessionCartService = sessionCartService;
        this.breadcrumbsService = breadcrumbsService;
    }


    @GetMapping("/top")
    public String topProductsView(Model model) {
        int recordLimit = 10;

        List<BreadcrumbItem> breadcrumbItems = breadcrumbsService.breadcrumbsHomeTop();
        model.addAttribute("breadcrumbs", breadcrumbItems);

        List<Product> topProducts = productService.getTopProducts(recordLimit);
        model.addAttribute("topProducts", topProducts);
        return "product/top";
    }

    @GetMapping("/newest")
    public String newestProductsView(Model model, Pageable pageable) {
        int recordLimit = 30;

        List<BreadcrumbItem> breadcrumbItems = breadcrumbsService.breadcrumbsHomeNewest();
        model.addAttribute("breadcrumbs", breadcrumbItems);

        Page<Product> newestProductsPage = productService.getNewestProducts(recordLimit, pageable);

        model.addAttribute("newestProductsPage", newestProductsPage);
        paging(model, newestProductsPage);

        return "product/newest";
    }

    @GetMapping("categories/{category-id}/products")
    public String productsByCategoryView(@PathVariable("category-id") UUID categoryId,
                                         @RequestParam(name = "field", required = false, defaultValue = "name") String field,
                                         @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
                                         @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                         @RequestParam(name = "size", required = false, defaultValue = "12") int size,
                                         Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);

        String reverseSort = null;
        if ("asc".equals(direction)) {
            reverseSort = "desc";
        } else {
            reverseSort = "asc";
        }

        Category category = categoryService.getCategory(categoryId);
        List<BreadcrumbItem> breadcrumbItems = breadcrumbsService.breadcrumbsHomeCategoriesCategoriesName(category.getName(), categoryId);
        model.addAttribute("breadcrumbs", breadcrumbItems);


        Page<Product> productsPage = productService.findProductByCategoryId(categoryId, pageable);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("category", category);

        paging(model, productsPage);
        return "product/list";
    }

    @PostMapping("/product/add/{product-id}")
    public String addNewestProductToCart(
            @PathVariable("product-id") UUID productId,
            Model model,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Product product = productService.getSingleProduct(productId);
        sessionCartService.addProductToCart(product);


        redirectAttributes.addFlashAttribute("message", "Product has been added to cart!");

        // Pobranie poprzedniego URL za pomocą nagłówka Referer
        String refererUrl = request.getHeader("Referer");

        // Warunkowe przekierowanie w zależności od poprzedniego URL
        if (refererUrl != null && refererUrl.contains("/newest")) {
            return "redirect:/newest";
        } else if (refererUrl != null && refererUrl.contains("/top")) {
            return "redirect:/top";
        }

        // Domyślne przekierowanie, jeśli nie ma dopasowania
        return "redirect:/";
    }

    @PostMapping("categories/{category-id}/products/add/{product-id}")
    public String addProductToCart(@PathVariable("product-id") UUID productId, @PathVariable("category-id") UUID categoryId,
                                   @RequestParam(name = "field", required = false, defaultValue = "name") String field,
                                   @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
                                   @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                   @RequestParam(name = "size", required = false, defaultValue = "12") int size,
                                   Model model, RedirectAttributes redirectAttributes) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);

        String reverseSort = null;
        if ("asc".equals(direction)) {
            reverseSort = "desc";
        } else {
            reverseSort = "asc";
        }

        Product product = productService.getSingleProduct(productId);
        sessionCartService.addProductToCart(product);

        Page<Product> productsPage = productService.findProductByCategoryId(categoryId, pageable);

        model.addAttribute("productsPage", productsPage);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("category", categoryService.getCategory(categoryId));

        redirectAttributes.addFlashAttribute("message", "Product has been added to cart!");

        paging(model, productsPage);
        return "redirect:/categories/{category-id}/products";
    }

    @PostMapping("add/{product-id}")
    public String addProductToCartInSingleView(
            @PathVariable("product-id") UUID productId,
            @RequestParam(name = "quantity", required = false, defaultValue = "1") int quantity, RedirectAttributes redirectAttributes,
            Model model) {


        Product product = productService.getSingleProduct(productId);
        for (int i = 0; i < quantity; i++) {
            sessionCartService.addProductToCart(product);
        }


        model.addAttribute("product", productService.getSingleProduct(productId));

        redirectAttributes.addFlashAttribute("message", Message.info("Product has been added to cart!"));
        return "redirect:/products/{product-id}";
    }


    @GetMapping("products/{product-id}")
    public String singleProductView(@PathVariable("product-id") UUID productId, Model model) {
        Product product = productService.getSingleProduct(productId);
        model.addAttribute("product", product);

        Category category = product.getCategory();
        List<BreadcrumbItem> breadcrumbItems = breadcrumbsService.breadcrumbsHomeCategoriesCategoriesName(category.getName(), category.getId());
        model.addAttribute("breadcrumbs", breadcrumbItems);

        return "product/single";
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
