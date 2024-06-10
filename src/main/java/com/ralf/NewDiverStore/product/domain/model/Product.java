package com.ralf.NewDiverStore.product.domain.model;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private UUID id;

    @NotBlank(message = "{divestore.validation.name.NotBlank.message}")
    @Size(min = 3, max = 255, message = "Size must be in range between 3 to 255 characters")
    private String name;

    @NotNull(message = "Price must be added")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Double price;

    @Size(max = 2550, message = "Size must not exceed 2550 characters")
    private String description;

    private String imagePath;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Producer producer;

    public Product() {
        this.id=UUID.randomUUID();
    }

    public Product(String name, double price) {
        this();
        this.name = name;
        this.price = price;
    }



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
