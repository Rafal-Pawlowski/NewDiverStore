package com.ralf.NewDiverStore.category.service;

import com.ralf.NewDiverStore.cart.domain.model.Cart;
import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.domain.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional
    public Category createCategory(Category categoryRequest) {
        Category category = new Category(categoryRequest.getName(),
                categoryRequest.getDescription());
                category.setImagePath(categoryRequest.getImagePath());
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Page<Category> getCategories(Pageable pageable) {
        return getCategories(null, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Category> getCategories(String search, Pageable pageable) {
        if (search == null) {
            return categoryRepository.findAll(pageable);
        } else
            return categoryRepository.findByNameContainingIgnoreCase(search, pageable);
    }

    @Transactional(readOnly = true)
    public Category getCategory(UUID id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return category;
        } else {
            throw new EntityNotFoundException("Category with id: " + id + " not found");
        }
    }

    @Transactional
    public Category getCategoryByName(String name){
        try{
            Category category = categoryRepository.findByNameContaining(name);
            return category;
        } catch (Exception e){
            throw new EntityNotFoundException("Cannot find category with name: "+ name);
        }
    }

    @Transactional
    public Category updateCategory(UUID id, Category categoryRequest) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            category.setImagePath(categoryRequest.getImagePath());
            return categoryRepository.save(category);
        } else {
            throw new EntityNotFoundException("Category with id: " + id + " not found");
        }
    }

    @Transactional
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }
}
