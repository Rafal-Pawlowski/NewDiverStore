package com.ralf.NewDiverStore.product.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.common.dto.Message;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductViewController {


    private final ProductService productService;

    private final CategoryService categoryService;

    private final SessionCartService sessionCartService;



    public ProductViewController(ProductService productService, CategoryService categoryService, SessionCartService sessionCartService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.sessionCartService = sessionCartService;
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

        Page<Product> productsPage = productService.findProductByCategoryId(categoryId, pageable);
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);
        model.addAttribute("category", categoryService.getCategory(categoryId));

        paging(model, productsPage);
        return "product/list";
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
            @RequestParam(name = "quantity", required = false, defaultValue = "1")int quantity, RedirectAttributes redirectAttributes,
            Model model){



        Product product = productService.getSingleProduct(productId);
        for(int i = 0; i < quantity; i++) {
            sessionCartService.addProductToCart(product);
        }



        model.addAttribute("product", productService.getSingleProduct(productId));

        redirectAttributes.addFlashAttribute("message", Message.info("Product has been added to cart!"));
        return "redirect:/products/{product-id}";
    }




    @GetMapping("products/{product-id}")
    public String singleProductView(@PathVariable("product-id") UUID productId, Model model) {
        model.addAttribute("product", productService.getSingleProduct(productId));

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
