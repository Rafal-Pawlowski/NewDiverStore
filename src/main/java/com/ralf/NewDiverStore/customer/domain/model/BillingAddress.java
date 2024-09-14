package com.ralf.NewDiverStore.customer.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BillingAddress extends Address{

    @Id
    private UUID id;

}
