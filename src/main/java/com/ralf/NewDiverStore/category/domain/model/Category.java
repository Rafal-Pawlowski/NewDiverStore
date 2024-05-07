package com.ralf.NewDiverStore.category.domain.model;

import com.ralf.NewDiverStore.producer.domain.model.Producer;
import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;


    public Category() {
        this.id=UUID.randomUUID();
    }

    public Category(String name) {
        this();
        this.name = name;
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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
