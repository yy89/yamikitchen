package com.xiaobudian.yamikitchen.web.dto;

import com.xiaobudian.yamikitchen.domain.User;

/**
 * Created by Johnson on 2015/4/23.
 */
public class UserResponse {
    private String token;
    private User user;

    public UserResponse() {
    }

    public UserResponse(String token, User user) {
        this();
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
