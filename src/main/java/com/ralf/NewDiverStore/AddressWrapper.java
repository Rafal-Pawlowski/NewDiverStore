package com.ralf.NewDiverStore;

import com.ralf.NewDiverStore.customer.domain.model.BillingAddress;
import com.ralf.NewDiverStore.customer.domain.model.ShippingAddress;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressWrapper {
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;


    public AddressWrapper(){
        new ShippingAddress();
        new BillingAddress();
    }

}
