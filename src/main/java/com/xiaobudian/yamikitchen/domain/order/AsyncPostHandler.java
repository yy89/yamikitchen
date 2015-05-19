package com.xiaobudian.yamikitchen.domain.order;

import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Johnson on 2015/5/15.
 */
@Component(value = "asyncPostHandler")
public class AsyncPostHandler {
    @Inject
    private ProductRepository productRepository;

    public void handle(Order order, List<OrderItem> items) {
        for (OrderItem item : items) {
            Product p = productRepository.findOne(item.getProductId());
            p.setSoldCount(p.getSoldCount() + 1);
            if (order.isToday()) p.setRestCount(p.getRestCount() - 1);
            if (order.isTomorrow()) p.setTwRestCount(p.getTwRestCount() - 1);
            productRepository.save(p);
        }
    }
}
