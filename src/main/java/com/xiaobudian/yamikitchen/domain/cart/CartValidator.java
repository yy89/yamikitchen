package com.xiaobudian.yamikitchen.domain.cart;

import com.xiaobudian.yamikitchen.common.LocalizedMessageSource;
import com.xiaobudian.yamikitchen.domain.merchant.Product;
import com.xiaobudian.yamikitchen.domain.order.OrderItem;
import com.xiaobudian.yamikitchen.repository.merchant.ProductRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnson on 2015/5/15.
 */
@Component(value = "cartValidator")
public class CartValidator {
    @Inject
    private ProductRepository productRepository;
    @Inject
    private LocalizedMessageSource localizedMessageSource;

    private boolean isSoldOut(Product product, OrderItem item, boolean isToday) {
        return product.isSoldOut(isToday) ||
                (isToday && product.getRestCount() < item.getQuantity()) ||
                (!isToday && product.getTwRestCount() < item.getQuantity());
    }

    public List<String> validate(Cart cart) {
        List<String> errors = new ArrayList<>();
        if (cart == null) {
            errors.add(localizedMessageSource.getMessage("order.cart.empty"));
            return errors;
        }
        for (OrderItem item : cart.getItems()) {
            Product p = productRepository.findOne(item.getProductId());
            if (isSoldOut(p, item, cart.isToday()))
                errors.add(localizedMessageSource.getMessage("order.product.sold.out"));
        }
        return errors;
    }
}
