package com.ralf.NewDiverStore.category.service;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.category.domain.repository.CategoryRepository;
import com.ralf.NewDiverStore.product.domain.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void clearDatabase() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }


    @Test
    void shouldCreateCategory() {
        //given
        Category category = new Category("Category 1", "description");

        //when
        Category result = categoryService.createCategory(category);

        //then
    assertThat(result)
            .isNotNull()
            .extracting(Category::getName, Category::getDescription)
            .containsExactlyInAnyOrder(category.getName(), category.getDescription());

        Optional<Category> optionalCategory = categoryRepository.findById(result.getId());
        assertThat(optionalCategory)
                .isPresent()
                .get()
                .extracting(Category::getName, Category::getDescription)
                .containsExactly(result.getName(), result.getDescription());
    }

    @Test
    void shouldGetCategories() {
        //given

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
    void shouldGetCategoriesWithSearchQuery() {
        //given
        String query = "abc";
        Category category1 = new Category("Category 1");
        Category category2 = new Category("Category 2" + query);
        Category category3 = new Category("Category 3");

        categoryRepository.saveAll(List.of(category1, category2, category3));

        //when
        Page<Category>result = categoryService.getCategories(query, Pageable.unpaged());

        //then
        assertThat(result)
                .hasSize(1)
                .extracting(Category::getId)
                .containsExactlyInAnyOrder(category2.getId());

    }

    @Test
    void shouldGetCategory() {
        //given
        Category category = new Category("Category 2");
        categoryRepository.saveAll(List.of(
                new Category("Category 1"),
                category,
                new Category("Category 3")));

        //when
        Category result = categoryService.getCategory(category.getId());

        //then
        assertThat(result.getName()).isEqualTo(category.getName());
        assertThat(result.getId()).isEqualTo(category.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGettingCategory(){
        //given
        UUID id = UUID.randomUUID();

        //when & then
      EntityNotFoundException exception =  assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getCategory(id);
        });
      assertThat(exception.getMessage()).isEqualTo("Category with id: " + id + " not found");

    }


    @Test
    void shouldGetCategoryByName() {
        //given
        String nameQuery = "One";
        Category category = new Category("CategoryOne");
        categoryRepository.saveAll(List.of(
                category,
                new Category("CategoryTwo"),
                new Category("CategoryThree")));
        //when
        Category result = categoryService.getCategoryByName(nameQuery);

        //then
        assertThat(result.getName()).isEqualTo(category.getName());
        assertThat(result.getId()).isEqualTo(category.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenGettingCategoryByName(){
        //given
        String nameQuery = "One";

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getCategoryByName(nameQuery);
        });
        assertThat(exception.getMessage()).isEqualTo("Cannot find category with name: "+ nameQuery);

    }

    @Test
    void shouldUpdateCategory() {
        //given
        Category category = categoryService.createCategory(new Category("Category 1", "Test Category Description"));

        Category categoryRequest = new Category("Category Test", "New Description");
        categoryRequest.setImagePath("testImagePath");

        //when
        Category result = categoryService.updateCategory(category.getId(), categoryRequest);

        //then
        assertThat(result)
                .isNotNull()
                .extracting(Category::getId, Category::getName, Category::getDescription, Category::getImagePath)
                .containsExactly(category.getId(), "Category Test", "New Description", "testImagePath");

    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUpdatingCategory(){
        //given
        UUID id = UUID.randomUUID();
        Category categoryRequest = new Category("Category Test", "New Description");

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.updateCategory(id, categoryRequest);
        });
        assertThat(exception.getMessage()).isEqualTo("Category with id: " + id + " not found");
    }

    @Test
    void shouldDeleteCategory() {
        //given
        Category category = categoryService.createCategory(new Category("Category to delete"));
        UUID id = category.getId();
        //when
        Throwable throwable = catchThrowable(()-> categoryService.deleteCategory(id));

        //then
        assertThat(throwable).isNull();
        assertThat(categoryRepository.findById(id)).isEmpty();
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenRemovingCategory(){
        //given
        UUID id = UUID.randomUUID();

        //when & then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.deleteCategory(id);
        });
        assertThat(exception.getMessage()).isEqualTo("Category with id: " + id + " not exist");

    }
}