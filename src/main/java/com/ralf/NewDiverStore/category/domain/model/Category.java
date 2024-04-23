package com.ralf.NewDiverStore.category.domain.model;

import java.util.UUID;

public class Category {

    private UUID id;

    private String name;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
        this.id=UUID.randomUUID();
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
