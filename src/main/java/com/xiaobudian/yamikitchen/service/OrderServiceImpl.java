package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.*;
import com.xiaobudian.yamikitchen.util.Constants;
import com.xiaobudian.yamikitchen.util.DateUtils;
import com.xiaobudian.yamikitchen.web.dto.OrderRequest;
import com.xiaobudian.yamikitchen.web.dto.OrderResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private OrderItemRepository orderItemRepository;
    @Inject
    private UserAddressRepository userAddressRepository;
    @Inject
    private CouponRepository couponRepository;

    private List<Integer> handingStatus = new ArrayList<Integer>(){
        {add(2);}//�ȴ�ȷ��
        {add(3);}//�ȴ�����
    };

    private List<Integer> solvedStatus = new ArrayList<Integer>(){
        {add(5);}// ������ɴ�����
        {add(6);}// �������������
    };

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
    public List<Order> getTodayPandingOrdersBy(int page, int pageSize, long rid) {
        Date todayStart = DateUtils.getTodayStart();
        Date todayEnd = DateUtils.getTodayStart();
        Sort sort = new Sort(Sort.Direction.ASC, "status").and(new Sort(Sort.Direction.DESC, "createDate"));
        PageRequest pageRequest = new PageRequest(page,pageSize,sort);
        return orderRepository.findByMerchantIdAndStatusInAndExpectDateBetween(rid,handingStatus,todayStart,todayEnd,pageRequest);
    }

    @Override
    public List<Order> getTodayCompletedOrdersBy(int page, int pageSize, long rid) {
        Date todayStart = DateUtils.getTodayStart();
        Date todayEnd = DateUtils.getTodayStart();
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        PageRequest pageRequest = new PageRequest(page,pageSize,sort);
        return orderRepository.findByMerchantIdAndStatusInAndExpectDateBetween(rid,solvedStatus,todayStart,todayEnd,pageRequest);
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
    
    @Override
	public Object getUnconfirmedOrders(Long uid) {
    	Assert.notNull(uid, "param can't be null : uid");
    	List<Object[]> resultList = orderRepository.getUnconfirmedOrders(uid);
    	if (CollectionUtils.isEmpty(resultList)) {
    		return null;
    	}
    	Map<Order, List<OrderItem>> order2OrderItemMap = new HashMap<Order, List<OrderItem>>();
    	for (Object[] objs : resultList) {
    		Order order = (Order) objs[0];
    		OrderItem orderItem = (OrderItem) objs[1];
    		List<OrderItem> orderItemList = order2OrderItemMap.get(order);
    		if (CollectionUtils.isEmpty(orderItemList)) {
    			orderItemList = new ArrayList<OrderItem>();
    			orderItemList.add(orderItem);
    			order2OrderItemMap.put(order, orderItemList);
    			continue;
    		}
    		orderItemList.add(orderItem);
    	}
    	
    	List<OrderResponse> orderResponseList = new ArrayList<OrderResponse>();
    	for (Order order : order2OrderItemMap.keySet()) {
    		OrderResponse orderResponse = new OrderResponse();
    		orderResponse.setOrder(order);
    		orderResponse.setOrderItems(order2OrderItemMap.get(order));
    		orderResponseList.add(orderResponse);
    	}
		return orderResponseList;
	}
    
    @Override
    public Order confirmOrder(Long uid, Long orderId) {
    	Assert.notNull(uid, "param can't be null : uid");
    	Assert.notNull(orderId, "param can't be null : orderId");
    	Order order = orderRepository.getOrderById(orderId);
		Assert.notNull(order, "Order not longer exist");
		Assert.isTrue(uid.equals(order.getUid()), "uid mismatching");
		if (Constants.DELIVER_METHOD_0 == order.getDeliverMethod()) {
			order.setStatus(Constants.ORDER_STATUS_3);
		} else if (Constants.DELIVER_METHOD_1 == order.getDeliverMethod()) {
			order.setStatus(Constants.ORDER_STATUS_6);
		}
		//TODO 有2个字段目前没有，后续加上需要赋值，详情参考产品原型说明
		try {
			orderRepository.save(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return order;
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
