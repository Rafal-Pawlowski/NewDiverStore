package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.cart.service.SessionCartService;
import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import com.ralf.NewDiverStore.product.domain.model.Product;
import com.ralf.NewDiverStore.product.service.ProductService;
import com.ralf.NewDiverStore.utilities.BreadcrumbItem;
import com.ralf.NewDiverStore.utilities.BreadcrumbsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CategoryViewController {

    private final CategoryService categoryService;
    private final ProductService productService;

    private final SessionCartService sessionCartService;

    private final BreadcrumbsService breadcrumbsService;


    public CategoryViewController(CategoryService categoryService, ProductService productService, SessionCartService sessionCartService, BreadcrumbsService breadcrumbsService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.sessionCartService = sessionCartService;
        this.breadcrumbsService = breadcrumbsService;
    }

    @ModelAttribute("sessionCartService")
    public SessionCartService sessionCartService() {
        return sessionCartService;
    }


    @GetMapping("index")
    public String indexView(Model model) {
        sessionCartService().getCart(); // in order to session start

        //articles
        model.addAttribute("categoryDrySuit", categoryService.getCategoryByName("Dry suits"));
        model.addAttribute("categoryMasks", categoryService.getCategoryByName("Masks"));
        model.addAttribute("categoryFins", categoryService.getCategoryByName("Fins"));
        return "category/index";
    }

    @GetMapping("categories")
    public String categoriesView(
            @RequestParam(name = "field", required = false, defaultValue = "name") String field,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "12") int size,
            Model model
    ) {
        List<BreadcrumbItem> breadcrumbItems = breadcrumbsService.breadcrumbsHomeCategories();
        model.addAttribute("breadcrumbs", breadcrumbItems);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);

        String reverseSort = null;
        if ("asc".equals(direction)) {
            reverseSort = "desc";
        } else {
            reverseSort = "asc";
        }

        Page<Category> categoriesPage = categoryService.getCategories(pageable);
        model.addAttribute("categoriesPage", categoriesPage);
        model.addAttribute("reverseSort", reverseSort);
        model.addAttribute("field", field);
        model.addAttribute("direction", direction);


        paging(model, categoriesPage);

        return "category/list";
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



