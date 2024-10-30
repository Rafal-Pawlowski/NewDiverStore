package com.ralf.NewDiverStore.producer.domain.model;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "producers")
@Setter
@Getter
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
    private Set<Product> products = new LinkedHashSet<>();


    public Producer() {
        this.id = UUID.randomUUID();
    }

    public Producer(String name) {
        this();
        this.name = name;
    }

    public Producer addProduct(Product product){     //is already used in another class!

        product.setProducer(this);
        products.add(product);

        return this;
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(products);
    }


    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
