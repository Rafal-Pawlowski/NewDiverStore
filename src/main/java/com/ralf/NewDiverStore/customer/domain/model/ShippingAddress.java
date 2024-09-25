package com.ralf.NewDiverStore.customer.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
public class ShippingAddress {

    @Id
    private UUID id;

    private String street;

    private String country;


    private String city;

    private String zip;

    @OneToOne
    private Customer customer;

    public ShippingAddress() {
        this.id = UUID.randomUUID();
    }

    public ShippingAddress(String street, String country, String city, String zip) {
        this();
        this.street = street;
        this.country = country;
        this.city = city;
        this.zip = zip;
    }



}
