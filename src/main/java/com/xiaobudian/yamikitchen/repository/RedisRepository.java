package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.cart.Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Inject
    private RedisTemplate<String, Cart> cartRedisTemplate;
    @Inject
    private RedisTemplate<String, Long> longRedisTemplate;

    public void set(String key, String value, Long expireMinutes) {
        stringRedisTemplate.opsForValue().set(key, value, expireMinutes, TimeUnit.MINUTES);
    }

    public void setCart(String key, Cart value) {
        cartRedisTemplate.opsForValue().set(key, value);
    }

    public void setLong(String key, Long value) {
        longRedisTemplate.opsForValue().set(key, value);
    }

    public Long getLong(String key) {
        return longRedisTemplate.opsForValue().get(key);
    }

    public Cart getCart(String key) {
        return cartRedisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public String getWithDefault(String key, String defaultValue) {
        String v = stringRedisTemplate.opsForValue().get(key);
        return StringUtils.isEmpty(v) ? defaultValue : v;
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

    public Set<Long> membersAsLong(String key, long start, long end) {
        return longRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public void removeForZSet(String key, String value) {
        stringRedisTemplate.opsForZSet().remove(key, value);
    }

    public void removeKey(String key) {
        stringRedisTemplate.delete(key);
    }

    public Long nextLong(String key) {
        return stringRedisTemplate.opsForValue().increment(key, 1);
    }

    public void addForZSetWithScore(String key, Long value, double score) {
        longRedisTemplate.opsForZSet().add(key, value, score);
    }
}
