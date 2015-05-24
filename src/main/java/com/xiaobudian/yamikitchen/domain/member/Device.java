package com.xiaobudian.yamikitchen.domain.member;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/1/25.
 */
@Entity
public class Device implements Serializable {
    private static final int ANDROID_PLATFORM = 0;
    private static final long serialVersionUID = 400724062961100083L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer type = ANDROID_PLATFORM;
    private String uuid;
    private String channel;
    private String deviceToken;
    private Long uid;
    private Date createDate = new Date();

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Transient
    public boolean isAndroidPlatform() {
        return ANDROID_PLATFORM == type;
    }

    @Transient
    public boolean isDev() {
        return getChannel().contains("dev");
    }
}
