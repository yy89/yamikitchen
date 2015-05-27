package com.xiaobudian.yamikitchen.domain.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnson on 2015/1/27.
 */
public class PushMessage {
    @JsonIgnore
    private PushNotificationType type;
    private String content;
    private String payLoad;
    @JsonIgnore
    private Date createDate = new Date();

    public PushMessage() {
    }

    public PushMessage(PushNotificationType type, String content, String payLoad) {
        this.type = type;
        this.content = content;
        this.payLoad = payLoad;
    }

    public PushNotificationType getType() {
        return type;
    }

    public void setType(PushNotificationType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Map<String, Object> mapOfPushedPayLoad() {
        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("messageType", type.getId());
        result.put(type.getKey(), payLoad);
        return result;
    }
}
