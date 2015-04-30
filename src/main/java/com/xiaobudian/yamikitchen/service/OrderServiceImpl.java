package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.Order;
import com.xiaobudian.yamikitchen.domain.OrderItem;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.*;
import com.xiaobudian.yamikitchen.util.DateUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
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
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private OrderItemRepository orderItemRepository;
    private MerchantRepository merchantRepository;

    @Override
    public Cart addProductInCart(Long uid, Long rid, Long productId) {
        final String key = Keys.cartKey(uid);
        for (String itemKey : redisRepository.members(key)) {
            ItemKey k = ItemKey.valueOf(itemKey);
            if (k.product.equals(productId)) {
                redisRepository.removeForZSet(key, itemKey);
                redisRepository.addForZSet(key, Keys.cartItemKey(rid, productId, k.quality + 1));
                return getCart(uid);
            }
        }
        redisRepository.addForZSet(key, Keys.cartItemKey(rid, productId, 1));
        return getCart(uid);
    }

    @Override
    public Cart removeProductInCart(Long uid, Long rid, Long productId) {
        final String key = Keys.cartKey(uid);
        for (String itemKey : redisRepository.members(Keys.cartKey(uid))) {
            ItemKey k = ItemKey.valueOf(itemKey);
            if (k.getProduct().equals(productId)) {
                redisRepository.removeForZSet(key, itemKey);
                if (k.getQuality() > 1)
                    redisRepository.addForZSet(key, Keys.cartItemKey(rid, k.getProduct(), k.getQuality() - 1));
            }
        }
        return getCart(uid);
    }

    @Override
    public Cart getCart(Long uid) {
        List<OrderItem> items = new ArrayList<>();
        final String key = Keys.cartKey(uid);
        Cart cart = new Cart();
        cart.setUid(uid);
        for (String itemKey : redisRepository.members(key)) {
            ItemKey k = ItemKey.valueOf(itemKey);
            Product product = productRepository.findOne(k.getProduct());
            OrderItem orderItem = new OrderItem(product, k.getQuality());
            cart.setMerchantId(k.getRid());
            items.add(orderItem);
        }
        cart.setItems(items);
        cart.setMerchantName(merchantRepository.findOne(cart.getMerchantId()).getName());
        return cart;
    }

    @Override
    public List<Order> getOrdersByMerchantIdAndStatusAndCreateDateBetween(int page, int pageSize,long rid,int status,Date dateFrom,Date dateTo) {
        List<Integer> statuses = new ArrayList<>();
        statuses.add(status);
        return orderRepository.findByMerchantIdAndStatusInAndCreateDateBetween(rid, statuses, dateFrom, dateTo, new PageRequest(page, pageSize));
    }

    @Override
    public List<OrderItem> getItemsInOrder(String orderNo) {
        return orderItemRepository.findByOrderNo(orderNo);
    }

    @Override
    public boolean removeCart(Long uid) {
        final String key = Keys.cartKey(uid);
        redisRepository.removeKey(key);
        return true;
    }

    @Override
    public List<Order> getTodayHandingOrdersBy(int page, int pageSize, long rid) {
        Date todayStart = DateUtils.getTodayStart();
        Date todayEnd = DateUtils.getTodayEnd();
        List<Integer> handingStatus = new ArrayList<Integer>(){
            {add(1);}
            {add(2);}
            {add(3);}
        };
        PageRequest pageRequest = new PageRequest(page,pageSize,new Sort(new Sort.Order(Sort.Direction.DESC,"status"),new Sort.Order(Sort.Direction.ASC,"createDate")));
        return orderRepository.findByMerchantIdAndStatusInAndCreateDateBetween(rid, handingStatus, todayStart, todayEnd, pageRequest);
    }

    @Override
     public List<Order> getTodaySolvedOrdersBy(int page, int pageSize, long rid) {
        Date todayStart = DateUtils.getTodayStart();
        Date todayEnd = DateUtils.getTodayEnd();
        int solvedStatus = 0;
        PageRequest pageRequest = new PageRequest(page,pageSize,new Sort(new Sort.Order(Sort.Direction.DESC,"createDate")));
        return orderRepository.findByMerchantIdAndStatusAndCreateDateBetween(rid,solvedStatus,todayStart,todayEnd,pageRequest);
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
