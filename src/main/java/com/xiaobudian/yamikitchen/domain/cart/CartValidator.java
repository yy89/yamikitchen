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

    public List<String> validate(Cart cart) {
        List<String> errors = new ArrayList<>();
        if (cart == null) {
            errors.add(localizedMessageSource.getMessage("order.cart.empty"));
            return errors;
        }
        for (OrderItem item : cart.getItems()) {
            Product p = productRepository.findOne(item.getProductId());
            if (p.isSoldOut(cart.isToday())) errors.add(localizedMessageSource.getMessage("order.product.sold.out"));
        }
        return errors;
    }
}
