package com.xiaobudian.yamikitchen.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.domain.cart.Cart;
import com.xiaobudian.yamikitchen.domain.cart.Settlement;
import com.xiaobudian.yamikitchen.domain.http.HttpParams;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.domain.order.Order;
import com.xiaobudian.yamikitchen.domain.order.OrderDetail;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.repository.CouponRepository;
import com.xiaobudian.yamikitchen.repository.HttpClientRepository;
import com.xiaobudian.yamikitchen.repository.MerchantRepository;
import com.xiaobudian.yamikitchen.repository.OrderItemRepository;
import com.xiaobudian.yamikitchen.repository.OrderRepository;
import com.xiaobudian.yamikitchen.repository.ProductRepository;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import com.xiaobudian.yamikitchen.repository.UserAddressRepository;
import com.xiaobudian.yamikitchen.util.Constants;
import com.xiaobudian.yamikitchen.util.DateUtils;
import com.xiaobudian.yamikitchen.web.dto.DadaResultDto;
import com.xiaobudian.yamikitchen.web.dto.OrderRequest;

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
    @Inject
    private HttpClientRepository httpClientRepository;
    @Inject
    private HttpclientService httpclientService;

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
    public List<OrderDetail> getUnconfirmedOrders(Long uid, Date createDate) {
        Assert.notNull(uid, "param can't be null : uid");
        if (createDate == null) {
            return orderRepository.getUnconfirmedOrders(uid);
        } else {
            return orderRepository.getUnconfirmedOrders(uid, createDate);
        }
    }

    @Override
    public Order confirmOrder(Long uid, String orderNo) {
        Order order = checkRequestUser(uid, orderNo);
        if (Constants.DELIVER_METHOD_0 == order.getDeliverMethod()) {
            order.setStatus(Constants.ORDER_STATUS_3);
        } else if (Constants.DELIVER_METHOD_1 == order.getDeliverMethod()) {
            order.setStatus(Constants.ORDER_STATUS_6);
        }
        order.setDirectCancelable(false);
        order.setAcceptDate(new Date());
        try {
            orderRepository.save(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    @Transactional
    public Order chooseDeliverGroup(Long uid, String orderNo, Integer deliverGroup) {
    	Assert.notNull(deliverGroup, "params can't be null : deliverGroup");
        Order order = checkRequestUser(uid, orderNo);
        try {
			// deliverGroup配送机构：1自己配送  2达达配送
        	if (deliverGroup == 2) {
        		HttpParams httpParams = httpClientRepository.getHttpParamsById(1L);
        		if (httpParams == null) {
        			httpParams = new HttpParams();
        			setHttpParams(httpParams);
        		} else if (!tokenIsValid(httpParams.getExpiresIn(), httpParams.getCreateData())) {
        			setHttpParams(httpParams);
        		}
        		
        		// 调用达达接口，添加订单
        		DadaResultDto dadaResultDto = httpclientService
        				.addOrderToDada(order, httpParams.getAccessToken());
        		if (dadaResultDto == null || !Constants.DADA_RESPONSE_STATUS_OK.equals(dadaResultDto.getStatus())) {
        			throw new RuntimeException("向达达添加订单出现异常");
        		}
        	}
        	order.setDeliverGroup(deliverGroup);
        	// 订单状态变成外卖配送中
    		order.setStatus(4);
    		// 记录外出配送时间
    		order.setOutDate(new Date());
        	orderRepository.save(order);
        	return order;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
    
    @Override
    public Order dadaCallBack(DadaResultDto dadaResultDto) {
    	Assert.notNull(dadaResultDto, "dadaResultDto can't be null");
    	Assert.notNull(dadaResultDto.getOrder_id(), "dadaResultDto.order_id can't be null");
    	Assert.notNull(dadaResultDto.getOrder_status(), "dadaResultDto.order_status can't be null");
    	
    	Order order = orderRepository.getOrderByOrderNo(dadaResultDto.getOrder_id());
    	Assert.notNull(order, "Order not longer exist ： " + dadaResultDto.getOrder_id());
    	order.setStatus(dadaResultDto.getOrder_status());
    	order.setDiliverymanId(dadaResultDto.getDm_id());
    	order.setDiliverymanMobile(dadaResultDto.getDm_mobile());
    	order.setUpdateTime(new Date(dadaResultDto.getUpdate_time()));
    	return orderRepository.save(order);
    }
    
    /**
     * 保存达达返回的系统级参数
     * @param httpParams
     * @author Liuminglu
     * @Date 2015年5月18日 上午10:18:09
     */
    private void setHttpParams(HttpParams httpParams) {
    	DadaResultDto dadaResultDto = httpclientService.getAccessToken();
		httpParams.setAccessToken(dadaResultDto.getResult().getAccess_token());
		httpParams.setExpiresIn(dadaResultDto.getResult().getExpires_in());
		httpParams.setRefreshToken(dadaResultDto.getResult().getRefresh_token());
		httpParams.setCreateData(new Date());
		httpClientRepository.save(httpParams);
    }
    
    /**
     * 验证达达接口token是否有效
     * @param expiresIn
     * @param createData
     * @return  true:有效  false:失效
     * @author Liuminglu
     * @Date 2015年5月15日 下午3:26:46
     */
    private boolean tokenIsValid(Long expiresIn, Date createData) {
    	Long timestamp = createData.getTime() + expiresIn;
    	Date date = new Date(timestamp);
    	return date.after(new Date());
    }

    /**
     * 验证uid的所属商户是否有权限修改orderId订单
     * 商户只能修改自己接到的订单，服务端加此验证防止请求被拦截参数被篡改
     *
     * @param uid 被验证的商户uid
     * @param orderNo 商户请求中要修改的订单编号
     * @author Liuminglu
     * @Date 2015年5月13日 下午3:34:57
     */
    private Order checkRequestUser(Long uid, String orderNo) {
        Assert.notNull(uid, "param can't be null : uid");
        Assert.notNull(orderNo, "param can't be null : orderNo");
        Order order = orderRepository.getOrderByOrderNo(orderNo);
        Assert.notNull(order, "Order not longer exist : " + orderNo);
        Assert.isTrue(uid.equals(order.getMerchantId()), "uid mismatching");
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
