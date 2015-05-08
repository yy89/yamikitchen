package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.LocalizedMessageSource;
import com.xiaobudian.yamikitchen.domain.User;
import com.xiaobudian.yamikitchen.domain.merchant.UserAddress;
import com.xiaobudian.yamikitchen.repository.UserAddressRepository;
import com.xiaobudian.yamikitchen.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Johnson on 2015/4/23.
 */
@Service(value = "memberService")
public class MemberServiceImpl implements MemberService {
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private UserRepository userRepository;
    @Inject
    private LocalizedMessageSource localizedMessageSource;
    @Inject
    private UserAddressRepository userAddressRepository;

    @Override
    public User register(User user) {
        if (StringUtils.isEmpty(user.getNickName())) {
            String nickNamePrefix = localizedMessageSource.getMessage("user.name.default");
            user.setNickName(nickNamePrefix + StringUtils.substring(user.getUsername(), user.getUsername().length() - 4));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserBy(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User changePassword(User user) {
        User u = userRepository.findByUsername(user.getUsername());
        u.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(u);
    }

    @Override
    public List<UserAddress> getAddresses(Long uid) {
        return userAddressRepository.findByUid(uid);
    }

    @Override
    public UserAddress addAddress(UserAddress userAddress) {
        return userAddressRepository.save(userAddress);
    }

    @Override
    public User getUser(Long creator) {
        return userRepository.findOne(creator);
    }

    @Override
    public boolean removeAddress(Long addressId) {
        userAddressRepository.delete(addressId);
        return true;
    }

    @Override
    public User updateUserIntroduction(User user) {
        User userdb = userRepository.findOne(user.getId());
        if(StringUtils.isNotEmpty(user.getHeadPic())){
            userdb.setHeadPic(user.getHeadPic());
        }
        if(StringUtils.isNotEmpty(user.getUsername())){
            userdb.setUsername(user.getUsername());
        }
        if(user.getGender()!=null){
            userdb.setGender(user.getGender());
        }
        if(StringUtils.isNotEmpty(user.getRegion())){
            userdb.setRegion(user.getRegion());
        }
        if(StringUtils.isNotEmpty(user.getDescription())){
            userdb.setDescription(user.getDescription());
        }
        userRepository.save(userdb);
        return userdb;
    }
}
