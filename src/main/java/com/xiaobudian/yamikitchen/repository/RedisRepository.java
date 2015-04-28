package com.xiaobudian.yamikitchen.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Created by Johnson on 2015/4/23.
 */
@Repository(value = "redisRepository")
public class RedisRepository {
    @Inject
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value, Long expireMinutes) {
        stringRedisTemplate.opsForValue().set(key, value, expireMinutes, TimeUnit.MINUTES);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


}
