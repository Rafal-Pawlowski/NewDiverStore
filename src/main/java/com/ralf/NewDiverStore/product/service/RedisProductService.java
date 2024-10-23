package com.ralf.NewDiverStore.product.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RedisProductService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisProductService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long getOrderCount(UUID productId) {
        String redisKey = "product:order_count:" + productId.toString();
        String orderCount = redisTemplate.opsForValue().get(redisKey);
        return orderCount != null ? Long.valueOf(orderCount) : 0;
    }

    public void setOrderCount(UUID productId, Long count) {
        String redisKey = "product:order_count:" + productId.toString();
        redisTemplate.opsForValue().set(redisKey, count.toString());
    }
}