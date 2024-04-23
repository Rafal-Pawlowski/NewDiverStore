package com.ralf.NewDiverStore.product.domain.model;

import java.util.UUID;

public class Product {

    private UUID id;

    private String name;

    private double price;

    public Product() {
        this.id=UUID.randomUUID();
    }

    public Product(String name, double price) {
        this();
        this.name = name;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
