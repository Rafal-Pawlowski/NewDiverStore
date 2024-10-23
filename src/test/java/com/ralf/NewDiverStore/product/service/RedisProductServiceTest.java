package com.ralf.NewDiverStore.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RedisProductServiceTest {

    @Autowired
    private RedisProductService redisProductService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void shouldSetAndGetOrderCount() {
        // given
        UUID productId = UUID.randomUUID();
        Long orderCount = 15L;

        // when
        redisProductService.setOrderCount(productId, orderCount);
        Long retrievedOrderCount = redisProductService.getOrderCount(productId);

        // then
        assertThat(retrievedOrderCount).isEqualTo(orderCount);
    }

    @Test
    void shouldReturnZeroWhenOrderCountNotSet() {
        // given
        UUID productId = UUID.randomUUID();

        // when
        Long retrievedOrderCount = redisProductService.getOrderCount(productId);

        // then
        assertThat(retrievedOrderCount).isEqualTo(0L);
    }
}