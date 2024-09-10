package com.ralf.NewDiverStore.shipping.model;

import java.math.BigDecimal;

public class Shipping {
    private static final BigDecimal SHIPPING_THRESHOLD= new BigDecimal("200");
    private static final BigDecimal SHIPPING_COST = new BigDecimal("20");

    public BigDecimal calculateShippingCost(BigDecimal cartTotal) {
        if(cartTotal.equals(BigDecimal.ZERO) ){
            return BigDecimal.ZERO;
        } else if(cartTotal.compareTo(SHIPPING_THRESHOLD) < 0  ){
            return SHIPPING_COST;
        } else {
            return BigDecimal.ZERO;
        }
    }
}
