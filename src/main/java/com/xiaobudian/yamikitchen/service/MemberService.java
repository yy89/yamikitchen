package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.domain.merchant.UserAddress;

import java.util.List;

/**
 * Created by Johnson on 2015/4/22.
 */
public interface MemberService {
    public User register(User user);

    public User getUserBy(String userName);

    public User changePassword(User user);

    public List<UserAddress> getAddresses(Long uid);

    public UserAddress addAddress(UserAddress userAddress);

    public User getUser(Long creator);

    public boolean removeAddress(Long addressId);

    public User updateUserIntroduction(User user);

}
