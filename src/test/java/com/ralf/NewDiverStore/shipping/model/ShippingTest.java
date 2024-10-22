package com.ralf.NewDiverStore.shipping.model;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ShippingTest {

    private Shipping shipping;

    @BeforeEach
    void setUp() {
        shipping = new Shipping();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "200", "201", "2029"})
    void shouldCalculateShippingCostNoShippingFee(String cartTotalStr) {
        //given
        BigDecimal cartTotal = new BigDecimal(cartTotalStr);
        BigDecimal noShippingFee = BigDecimal.ZERO;

        //when
        BigDecimal result = shipping.calculateShippingCost(cartTotal);

        //then
        assertThat(result).isEqualTo(noShippingFee);
    }

    @ParameterizedTest
    @CsvSource({"1, 20", "99, 20", "199, 20"})
    void shouldCalculateShippingCostWithShippingFee(String cartTotalStr, String expectedShippingFeeStr) {
        //given
        BigDecimal cartTotal = new BigDecimal(cartTotalStr);
        BigDecimal expectedShippingFee = new BigDecimal(expectedShippingFeeStr);


        //when
        BigDecimal result = shipping.calculateShippingCost(cartTotal);

        //then
        assertThat(result).isEqualTo(expectedShippingFeeStr);


    }
}