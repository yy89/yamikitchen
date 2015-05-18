package com.xiaobudian.yamikitchen.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Johnson on 2015/5/13.
 */
public class NoticeTemplate {
    @JsonProperty(value = "status")
    private int orderStatus;
    private String title;
    private String contentTemplate;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentTemplate() {
        return contentTemplate;
    }

    public void setContentTemplate(String contentTemplate) {
        this.contentTemplate = contentTemplate;
    }
}
