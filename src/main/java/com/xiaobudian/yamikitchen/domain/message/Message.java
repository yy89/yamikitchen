package com.xiaobudian.yamikitchen.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xiaobudian.yamikitchen.common.Keys;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/5/27.
 */
@Entity
public class Message implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private MessageType type;
    @JsonIgnore
    private Long receiver;
    private Long publisher;
    private String nickName;
    private String headPic;
    private String content;
    private Date createDate;
    @JsonIgnore
    private Date readDate = new Date();
    @JsonIgnore
    private boolean unread;
    private Long merchantId;
    private Long orderId;
    private Long commentId;

    public Message() {
    }

    public Message(MessageType type, Long receiver, String nickName, String headPic, Long publisher) {
        this.type = type;
        this.receiver = receiver;
        this.nickName = nickName;
        this.headPic = headPic;
        this.publisher = publisher;
        this.createDate = new Date();
    }

    public Message(MessageType type, Long receiver, String nickName, String headPic, Long publisher,
                   Long orderId, Long merchantId) {
        this(type, receiver, nickName, headPic, publisher);
        this.orderId = orderId;
        this.merchantId = merchantId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getPublisher() {
        return publisher;
    }

    public void setPublisher(Long publisher) {
        this.publisher = publisher;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String queueName() {
        return Keys.uidMessageQueue(receiver, type);
    }

    public String unreadQueueName() {
        return Keys.uidMessageUnreadQueue(receiver, type);
    }

}