package com.xiaobudian.yamikitchen.domain.message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Johnson on 2015/5/12.
 */
@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer type;
    private String title;
    private String content;
    private String orderNo;
    private Date createDate = new Date();
    private Long uid;
    private String url;

    public Notice() {
    }

    public Notice(Integer type, String title, String content, String oderNo, Long uid) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.orderNo = oderNo;
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean toSomebody() {
        return uid != null && uid > 0;
    }
}
