package com.ralf.NewDiverStore.customer.domain.model;

import java.util.UUID;

public class CustomerBuilder {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Address shippingAddress;
    private Address billingAddress;

    public CustomerBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public CustomerBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CustomerBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CustomerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerBuilder withShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public CustomerBuilder withBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
        return this;
    }

    public Customer build() {
        return new Customer(id, firstName, lastName, email, shippingAddress, billingAddress);
    }
}
