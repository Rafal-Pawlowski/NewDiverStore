package com.ralf.NewDiverStore.category.service;

import com.ralf.NewDiverStore.category.domain.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {


    public Category createCategory(Category category) {
        category.setId(UUID.randomUUID());


        return category;
    }

    public List<Category> getCategories() {
        List<Category> categories =List.of(new Category("Category1"), new Category("Category2"), new Category("Category3"));
        return categories;
    }

    public Category getCategory(UUID id) {
        return new Category("SingleCategory" + id);
    }

    public Category updateCategory(UUID id, Category categoryRequest) {
   Category category = new Category("Before update Category");
category.setName(categoryRequest.getName());
        return category;

    }

    public void deleteCategory(UUID id) {
    }
}
