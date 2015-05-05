package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.Order;
import com.xiaobudian.yamikitchen.domain.OrderItem;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.*;
import com.xiaobudian.yamikitchen.web.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by johnson1 on 4/28/15.
 */
@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {
    @Value(value = "${extra.field.name}")
    private String extraFieldName;
    @Value(value = "${extra.deliver.price}")
    private String deliverPrice;
    @Inject
    private RedisRepository redisRepository;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private MerchantRepository merchantRepository;
    @Inject
    private OrderRepository orderRepository;
    @Inject
    private UserAddressRepository userAddressRepository;
    @Inject
    private CouponRepository couponRepository;

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
        final String key = Keys.cartKey(uid);
        Set<String> itemKeys = redisRepository.members(key);
        if (CollectionUtils.isEmpty(itemKeys)) return null;
        List<OrderItem> items = new ArrayList<>();
        Cart cart = new Cart();
        cart.setUid(uid);
        for (String itemKey : itemKeys) {
            ItemKey k = ItemKey.valueOf(itemKey);
            Product product = productRepository.findOne(k.getProduct());
            OrderItem orderItem = new OrderItem(product, k.getQuality());
            cart.setMerchantId(k.getRid());
            items.add(orderItem);
        }
        cart.setItems(items);
        cart.setMerchantName(merchantRepository.findOne(cart.getMerchantId()).getName());
        cart.putExtra(extraFieldName, deliverPrice);
        return cart;
    }

    @Override
    public List<Order> getOrdersByMerchantIdAndStatusAndCreateDateBetween(int page, int pageSize, long rid, int status, Date dateFrom, Date dateTo) {
        return null;
    }

    @Override
    public List<OrderItem> getItemsInOrder(String orderNo) {
        return null;
    }

    @Override
    public boolean removeCart(Long uid) {
        final String key = Keys.cartKey(uid);
        redisRepository.removeKey(key);
        return true;
    }

    @Override
    public List<Order> getTodayHandingOrdersBy(int page, int pageSize, long rid) {
        return null;
    }

    @Override
    public List<Order> getTodaySolvedOrdersBy(int page, int pageSize, long rid) {
        return null;
    }

    @Override
    public Order initOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setStatus(0);
        order.setCouponId(orderRequest.getCouponId());
        order.setFirstDeal(true);
        order.setHasPaid(false);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setDeliverMethod(orderRequest.getDeliverMethod());
        return orderRepository.save(order);
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrders(Long uid) {
        return orderRepository.findByUid(uid);
    }

    @Override
    public Settlement getSettlement(Long uid) {
        Settlement settlement = new Settlement();
        settlement.setAddress(userAddressRepository.findByUidAndIsDefaultTrue(uid));
        settlement.setPaymentMethod(1);
        settlement.setCart(getCart(uid));
        settlement.setCoupon(couponRepository.findFirstByUid(uid));
        settlement.setDeliverDate(merchantRepository.findOne(settlement.getCart().getMerchantId()).getBusinessHours());
        return settlement;
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
