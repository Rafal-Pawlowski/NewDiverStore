package com.ralf.NewDiverStore.category.controller;

import com.ralf.NewDiverStore.category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.ralf.NewDiverStore.category.domain.model.Category;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryApiController {

    /*
    Create
    POST/ addCategory

    Read
    GET/getCategories
    GET/getCategory

    Update
    PUT/ updateCategory

    Delete
    DELETE/ deleteCategory

     */

    private final CategoryService categoryService;

    public CategoryApiController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Category> getCategories(){
        return categoryService.getCategories();
    }

    @GetMapping("{category-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Category getCategory(@PathVariable("{category-id}") UUID categoryId){
        return categoryService.getCategory(categoryId);
    }

    @PutMapping("{category-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Category updateCategory(@PathVariable("{category-id}") UUID categoryId,@RequestBody Category categoryRequest){
        return categoryService.updateCategory(categoryId, categoryRequest);
    }

    @DeleteMapping("{category-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("{category-id}") UUID categoryId){
        categoryService.deleteCategory(categoryId);
    }









}
