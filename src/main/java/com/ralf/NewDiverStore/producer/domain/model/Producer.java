package com.ralf.NewDiverStore.producer.domain.model;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "producers")
public class Producer {

    @Id
    private UUID id;

    @NotBlank(message = "{divestore.validation.name.NotBlank.message}")
    @Size(min=3, max=255, message = "Size must be in range between 3 to 255 characters")
    private String name;

    @Size(max = 2550, message = "Size must not exceed 2550 characters")
    private String description;

    @ManyToMany
    private Set<Category> categories;

    @OneToMany(mappedBy = "producer")
    private Set<Product> products;


    public Producer() {
        this.id = UUID.randomUUID();
    }

    public Producer(String name) {
        this();
        this.name = name;
    }

    public Producer addProduct(Product product){
        if (products == null){
            products = new LinkedHashSet<>();
        }
        product.setProducer(this);
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

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
