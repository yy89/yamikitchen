package com.xiaobudian.yamikitchen.domain.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

/**
 * Created by Johnson on 2015/5/10.
 */
public enum OrderStatus {
    WAIT_PAYMENT(1), WAIT_CONFIRMATION(2), WAIT_DELIVER(3), WAIT_SELF_TAKE(4), DELIVERING(5), COMPLETED(6), CANCELLED(7);

    private final int index;

    OrderStatus(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static OrderStatus from(int index) {
        for (OrderStatus status : values()) {
            if (status.getIndex() == index) return status;
        }
        return null;
    }

    public static Collection<Integer> inProgressStatuses() {
        Collection<Integer> result = new ArrayList<>();
        for (OrderStatus status : EnumSet.of(WAIT_PAYMENT, WAIT_CONFIRMATION, WAIT_DELIVER, DELIVERING)) {
            result.add(status.ordinal() + 1);
        }
        return result;
    }
}
