package com.ralf.NewDiverStore.category.domain.model;

import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
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


}
