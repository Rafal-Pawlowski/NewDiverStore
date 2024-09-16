package com.ralf.NewDiverStore.customer.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Address {

    private String street;
    private String country;
    private String city;
    private String zip;


    public void copyFrom(Address source) {
        this.setStreet(source.getStreet());
        this.setCity(source.getCity());
        this.setZip(source.getZip());
        this.setCountry(source.getCountry());
    }

}
