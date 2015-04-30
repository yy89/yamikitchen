package com.xiaobudian.yamikitchen.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Date;
import java.util.Set;
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

    public void addForZSet(String key, String value) {
        stringRedisTemplate.opsForZSet().add(key, value, new Date().getTime());
    }

    public Set<String> members(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public Set<String> members(String key) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, 0, -1);
    }

    public void removeForZSet(String key, String value) {
        stringRedisTemplate.opsForZSet().remove(key, value);
    }

    public void removeKey(String key) {
        stringRedisTemplate.delete(key);
    }
}
