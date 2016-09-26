package com.redis.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by songyigui on 2016/8/10.
 */
@Component
public class RedisCache {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void set(final String key, final String value) {
        redisTemplate.boundValueOps(key).set(value);
    }

    public String get(final String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean exist(final String key) {
        return redisTemplate.hasKey(key);
    }

    public void del(final String key) {
        redisTemplate.delete(key);
    }

    public void delAll(final String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }

    public Boolean expire(final String key, final long seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public void set(final String key, final String value, final long seconds) {
        redisTemplate.boundValueOps(key).set(value, seconds, TimeUnit.SECONDS);
    }
}
