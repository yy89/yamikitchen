package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.OrderItem;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.ProductRepository;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnson1 on 4/28/15.
 */
@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private ProductRepository productRepository;

    @Override
    public List<OrderItem> addProductInCart(Long uid, Long rid, Long productId) {
        final String key = Keys.cartKey(uid);
        for (String itemKey : redisRepository.members(key)) {
            ItemKey k = ItemKey.valueOf(itemKey);
            if (k.product.equals(productId)) {
                redisRepository.addForZSet(key, Keys.cartItemKey(rid, productId, k.quality + 1));
                return getItemsInCart(uid);
            }
        }
        redisRepository.addForZSet(key, Keys.cartItemKey(rid, productId, 1));
        return getItemsInCart(uid);
    }

    @Override
    public List<OrderItem> removeProductInCart(Long uid, Long rid, Long productId) {
        final String key = Keys.cartKey(uid);
        for (String itemKey : redisRepository.members(Keys.cartKey(uid))) {
            ItemKey k = ItemKey.valueOf(itemKey);
            if (k.getProduct().equals(productId)) {
                redisRepository.removeForZSet(key, itemKey);
                if (k.getQuality() > 1)
                    redisRepository.addForZSet(key, Keys.cartItemKey(rid, k.getProduct(), k.getQuality() - 1));
            }
        }
        return getItemsInCart(uid);
    }

    @Override
    public List<OrderItem> getItemsInCart(Long uid) {
        List<OrderItem> result = new ArrayList<>();
        final String key = Keys.cartKey(uid);
        for (String itemKey : redisRepository.members(key)) {
            ItemKey k = ItemKey.valueOf(itemKey);
            Product product = productRepository.findOne(k.getProduct());
            OrderItem orderItem = new OrderItem(product, k.getQuality());
            result.add(orderItem);
        }
        return result;
    }

    @Override
    public boolean removeCart(Long uid) {
        final String key = Keys.cartKey(uid);
        redisRepository.removeKey(key);
        return true;
    }

    static final class ItemKey {
        private static final String DELIMITER = ":";
        private Long rid;
        private Long product;
        private Integer quality;

        public ItemKey(Long rid, Long product, Integer quality) {
            this.rid = rid;
            this.product = product;
            this.quality = quality;
        }

        public Long getRid() {
            return rid;
        }

        public Long getProduct() {
            return product;
        }

        public Integer getQuality() {
            return quality;
        }

        public static ItemKey valueOf(String s) {
            String[] ps = s.split(DELIMITER);
            return new ItemKey(Long.valueOf(ps[1]), Long.valueOf(ps[3]), Integer.valueOf(ps[5]));
        }
    }
}
