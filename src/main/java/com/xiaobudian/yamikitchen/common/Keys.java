package com.xiaobudian.yamikitchen.common;

/**
 * Created by Johnson on 2015/4/23.
 */
public final class Keys {
    public static String mobileSmsKey(String mobilePhone) {
        return mobilePhone + ":code";
    }

    public static String cartItemKey(Long rid, Long product, Integer quality) {
        return String.format("r:%d:p:%d:q:%d", rid, product, quality);
    }

    public static String cartKey(Long uid) {
        return String.format("uid:%d:carts", uid);
    }
}
