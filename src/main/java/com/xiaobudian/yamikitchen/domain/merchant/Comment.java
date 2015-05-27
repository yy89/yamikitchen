package com.xiaobudian.yamikitchen.domain.merchant;

import com.xiaobudian.yamikitchen.domain.message.Message;
import com.xiaobudian.yamikitchen.domain.message.MessageType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/4/22.
 */
@Entity
public class Comment implements Serializable {
    private static final long serialVersionUID = -6999094968335332646L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long uid;
    @NotNull(message = "comment.orderId.not.empty")
    private Long orderId;
    private String nickName;
    private String headPic;
    private String content;
    @NotNull(message = "comment.star.not.empty")
    private Integer star;
    private Long merchantId;
    private String merchantName;
    private Long productId;
    private String productName;
    private Date publishDate;
    private boolean removed = false;
    private Long cid;
    private String replyNickName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getReplyNickName() {
        return replyNickName;
    }

    public void setReplyNickName(String replyNickName) {
        this.replyNickName = replyNickName;
    }


    public Message createMessage(Long receiver, Long orderId, Long merchantId) {
        Message message = new Message(cid != null ? MessageType.REPLY_COMMENT : MessageType.COMMENT, receiver, nickName, headPic, uid, orderId, merchantId);
        message.setCommentId(id);
        message.setContent(content);
        return message;
    }
}
