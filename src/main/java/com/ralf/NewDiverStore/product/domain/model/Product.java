package com.ralf.NewDiverStore.product.domain.model;

import com.ralf.NewDiverStore.category.domain.model.Category;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@ToString
public class Product {

    @Id
    private UUID id;

    @NotBlank(message = "{divestore.validation.name.NotBlank.message}")
    @Size(min = 3, max = 255, message = "Size must be in range between 3 to 255 characters")
    private String name;

    @NotNull(message = "Price must be added")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal price;

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

    public Product(String name, BigDecimal price) {
        this();
        this.name = name;
        this.price = price;
    }
}
