package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class CategoryViewController {

    public final CategoryService categoryService;

    public CategoryViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("index")
    public String indexView(){
        return "category/index";
    }

    @GetMapping("categories")
    public String categoriesView(
            @RequestParam(name = "field", required = false, defaultValue = "name") String field,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "page", required = false, defaultValue = "0")int page,
            @RequestParam(name = "size", required = false, defaultValue = "12")int size,
            Model model
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), field);

        String reverseSort = null;
        if("asc".equals(direction)){
            reverseSort="desc";
        }else {
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
