package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created by Johnson on 2015/5/10.
 */
@Component
public class OrderNoGenerator {
    private static final String ORDER_NO_PATTERN = "%s-%s-%d";
    private static final String ORDER_DATE_PATTERN = "yyyyMMdd";
    @Inject
    private RedisRepository redisRepository;

    public String getOrderNo(String merchantNo) {
        final String d = DateTime.now().toString(ORDER_DATE_PATTERN);
        final long index = redisRepository.nextLong(Keys.nextOrderNoKey(d, merchantNo));
        return String.format(ORDER_NO_PATTERN, merchantNo, d, index);
    }
}
