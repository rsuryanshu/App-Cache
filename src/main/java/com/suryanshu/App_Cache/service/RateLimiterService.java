package com.suryanshu.App_Cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    private static final int MAX_REQUESTS = 5;
    private static final int WINDOW_SECONDS = 60;

    public boolean isAllowed(String clientIp) {

        String key = "rate:" + clientIp;

        Integer count = redisTemplate.opsForValue().get(key);

        if (count == null) {
            // first request — set counter to 1, start expiry timer
            redisTemplate.opsForValue().set(key, 1, WINDOW_SECONDS, TimeUnit.SECONDS);
            return true;
        }

        if (count < MAX_REQUESTS) {
            // under limit — increment counter
            redisTemplate.opsForValue().increment(key);
            return true;
        }

        // over limit — block
        return false;
    }
}
