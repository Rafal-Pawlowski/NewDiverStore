package com.ralf.NewDiverStore.customer.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class BillingAddress{

    @Id
    private UUID id;
    private String street;
    private String country;
    private String city;
    private String zip;

    @OneToOne
    private Customer customer;

    public BillingAddress() {
        this.id = UUID.randomUUID();
    }

    public BillingAddress(String street, String country, String city, String zip) {
        this();
        this.street = street;
        this.country = country;
        this.city = city;
        this.zip = zip;
    }
}
