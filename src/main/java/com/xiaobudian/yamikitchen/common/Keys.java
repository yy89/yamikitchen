package com.xiaobudian.yamikitchen.common;

import com.xiaobudian.yamikitchen.domain.message.MessageType;

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

    public static String uidMessageQueue(Long uid, MessageType type) {
        final int code = type.isComment() ? MessageType.COMMENT.getCode() : type.getCode();
        return String.format("uid:%d:type:%d:messages", uid, code);
    }

    public static String uidMessageUnreadQueue(Long uid, MessageType type) {
        return String.format("uid:%d:type:%d:unread:messages", uid, type.getCode());
    }

    public static String commentMessageIdQueue(Long commentId) {
        return String.format("comment:%d:message", commentId);
    }

    public static String payingQueue() {
        return "paying.orders";
    }

    public static String deliveringQueue(Long orderId) {
        return String.format("delivering:order:%d", orderId);
    }

    public static String unPaidQueue(Long orderId) {
        return String.format("unPaid:order:%d", orderId);
    }
}
