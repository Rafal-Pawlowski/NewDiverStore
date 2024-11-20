package com.ralf.NewDiverStore.product.domain.model;

import com.ralf.NewDiverStore.category.domain.model.Category;
//import com.ralf.NewDiverStore.currency.domain.Currency;
import com.ralf.NewDiverStore.producer.domain.model.Producer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
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
//
//    @Enumerated(EnumType.STRING)
//    private Currency currency;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Producer producer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @DecimalMin(value = "0.0", message = "Discount must be a positive value")
    @DecimalMax(value = "100.0", message = "Discount cannot exceed 100%")
    @Column(precision = 10, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    public BigDecimal getDiscountedPrice() {
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = price.multiply(discountPercentage).divide(new BigDecimal("100"));
            BigDecimal discountedPrice = price.subtract(discount);
            return discountedPrice.setScale(2, RoundingMode.HALF_UP);
        }
        return price;
    }


    public Product() {
        this.id=UUID.randomUUID();
        this.discountPercentage = BigDecimal.ZERO;
//        this.currency = Currency.EUR;
    }

    public Product(String name, BigDecimal price) {
        this();
        this.name = name;
        this.price = price;
    }

    @PrePersist
    private void onCreate(){
        this.createdDate= new Date();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", category=" + (category != null ? category.getId() : "null") +
                ", producer=" + (producer != null ? producer.getId() : "null") +
                '}';
    }

}
