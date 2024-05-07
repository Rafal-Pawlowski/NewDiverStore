package com.ralf.NewDiverStore.producer.domain.model;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.product.domain.model.Product;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "producers")
public class Producer {

    @Id
    private UUID id;

    private String name;

    @ManyToMany
    private Set<Category> categories;

    @OneToMany(mappedBy = "producer")
    private List<Product> products;


    public Producer() {
        this.id = UUID.randomUUID();
    }

    public Producer(String name) {
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
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
