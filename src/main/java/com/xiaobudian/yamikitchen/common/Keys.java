package com.xiaobudian.yamikitchen.common;

/**
 * Created by Johnson on 2015/4/23.
 */
public final class Keys {
    public static String allUids() {
        return String.format("uids");
    }

    public static String mobileSmsKey(String mobilePhone) {
        return mobilePhone + ":code";
    }

    public static String uidCartKey(Long uid) {
        return String.format("uid:%d:cart", uid);
    }

    public static String nextMerchantNoKey() {
        return String.format("next:merchantNo");
    }

    public static String nextOrderNoKey(String merchantNo, String date) {
        return String.format("rid:%s:d:%s:next:orderNo", date, merchantNo);
    }

    public static String uidNoticeQueue(String uid) {
        return String.format("uid:%s:notices", uid);
    }

    public static String uidUnreadNoticeQueue(String uid) {
        return String.format("uid:%s:unread:notices", uid);
    }


}
