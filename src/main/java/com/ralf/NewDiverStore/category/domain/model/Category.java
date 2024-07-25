package com.ralf.NewDiverStore.category.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    private UUID id;

    @NotBlank(message = "{divestore.validation.name.NotBlank.message}")
    @Size(min = 3, max = 255, message = "Size must be in range between 3 to 255 characters")
    private String name;

    @Size(max = 2550, message = "Size must not exceed 2550 characters")
    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    private String imagePath;


    public Category() {
        this.id = UUID.randomUUID();
    }

    public Category(String name) {
        this();
        this.name = name;
    }

    public Category(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }


    public Category addProduct(Product product) {
        if (products == null) {
            products = new LinkedHashSet<>();
        }
        product.setCategory(this);
        products.add(product);

        return this;
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(products);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", products=" + products +
                '}';
    }
}
