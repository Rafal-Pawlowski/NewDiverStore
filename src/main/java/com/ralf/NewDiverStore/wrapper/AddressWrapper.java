package com.ralf.NewDiverStore.wrapper;

import com.ralf.NewDiverStore.customer.domain.model.BillingAddress;
import com.ralf.NewDiverStore.customer.domain.model.ShippingAddress;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressWrapper {


    private ShippingAddress shippingAddress;

    private BillingAddress billingAddress;


    public AddressWrapper(){
        this.shippingAddress= new ShippingAddress();
        this.billingAddress = new BillingAddress();
    }

}
