package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.LocalizedMessageSource;
import com.xiaobudian.yamikitchen.domain.member.RegistrationPostHandler;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.UserAddress;
import com.xiaobudian.yamikitchen.repository.member.UserAddressRepository;
import com.xiaobudian.yamikitchen.repository.member.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Johnson on 2015/4/23.
 */
@Service(value = "memberService")
@Transactional
public class MemberServiceImpl implements MemberService {
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private UserRepository userRepository;
    @Inject
    private LocalizedMessageSource localizedMessageSource;
    @Inject
    private UserAddressRepository userAddressRepository;
    @Inject
    private RegistrationPostHandler registrationPostHandler;

    @Override
    public User register(User user) {
        if (StringUtils.isEmpty(user.getNickName())) {
            String nickNamePrefix = localizedMessageSource.getMessage("user.name.default");
            user.setNickName(nickNamePrefix + StringUtils.substring(user.getUsername(), user.getUsername().length() - 4));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBindingPhone(user.getUsername());
        User newUser = userRepository.save(user);
        registrationPostHandler.handle(newUser);
        return newUser;
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
    public User updateUser(User user) {
        User oldUser = userRepository.findOne(user.getId());
        if (StringUtils.isNotEmpty(user.getHeadPic())) {
            oldUser.setHeadPic(user.getHeadPic());
        }
        if (StringUtils.isNotEmpty(user.getUsername())) {
            oldUser.setUsername(user.getUsername());
        }
        if (user.getGender() != null) {
            oldUser.setGender(user.getGender());
        }
        if (StringUtils.isNotEmpty(user.getRegion())) {
            oldUser.setRegion(user.getRegion());
        }
        if (StringUtils.isNotEmpty(user.getDescription())) {
            oldUser.setDescription(user.getDescription());
        }
        userRepository.save(oldUser);
        return oldUser;
    }
}
