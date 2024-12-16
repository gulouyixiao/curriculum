package com.curriculum.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description 使用redis存储验证码
 */
@Component
public class RedisCheckCodeStore {

    @Autowired
    RedisTemplate redisTemplate;

    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
