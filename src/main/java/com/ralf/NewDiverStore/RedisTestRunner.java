package com.ralf.NewDiverStore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTestRunner implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) throws Exception {

        redisTemplate.opsForValue().set("testKey", "Hello, Redis!");

        String value = redisTemplate.opsForValue().get("testKey");

        System.out.println("Value from Redis: "+ value );

        if("Hello, Redis!".equals(value)){
            System.out.println("Connection with Redis works fine!");

        }else {
            System.out.println("Problem occured with connection to Redis.");
        }
    }
}
