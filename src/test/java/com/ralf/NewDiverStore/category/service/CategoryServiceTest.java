package com.ralf.NewDiverStore.category.service;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.domain.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
    }


    @Test
    void createCategory() {
        //given
        Category category = new Category("Category 1");

        //when
        Category result = categoryService.createCategory(category);

        //then
        assertThat(result.getName()).isEqualTo(category.getName());
        assertThat(result.getName()).isEqualTo(categoryService.getCategory(result.getId()).getName());

        Optional<Category> optionalCategory = categoryRepository.findById(result.getId());
        assertThat(result.getName()).isEqualTo(optionalCategory.get().getName());

    }

    @Test
    void getCategories() {
        //given
        categoryRepository.deleteAll();
        categoryRepository.saveAll(List.of(
                new Category("Category1"),
                new Category("Category2"),
                new Category("Category3")
        ));

        //when
        Page<Category> categories = categoryService.getCategories(Pageable.unpaged());

        //then
        assertThat(categories)
                .hasSize(3)
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Category1", "Category2", "Category3");

    }

    @Test
    void testGetCategories() {
    }

    @Test
    void getCategory() {
    }

    @Test
    void getCategoryByName() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }
}