package com.xiaobudian.yamikitchen.service;

import com.xiaobudian.yamikitchen.common.LocalizedMessageSource;
import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.member.BankCard;
import com.xiaobudian.yamikitchen.domain.member.RegistrationPostHandler;
import com.xiaobudian.yamikitchen.domain.member.User;
import com.xiaobudian.yamikitchen.domain.merchant.Merchant;
import com.xiaobudian.yamikitchen.domain.merchant.UserAddress;
import com.xiaobudian.yamikitchen.repository.member.BankCardRepository;
import com.xiaobudian.yamikitchen.repository.member.UserAddressRepository;
import com.xiaobudian.yamikitchen.repository.member.UserRepository;
import com.xiaobudian.yamikitchen.repository.merchant.MerchantRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    @Inject
    private BankCardRepository bankCardRepository;
    @Inject
    private MerchantRepository merchantRepository;

    @Override
    public User register(User user) {
        if (StringUtils.isEmpty(user.getNickName())) {
            String nickNamePrefix = localizedMessageSource.getMessage("user.name.default");
            user.setNickName(nickNamePrefix + StringUtils.substring(user.getUsername(), user.getUsername().length() - 4));
        }
        user.init();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        registrationPostHandler.handle(newUser);
        return newUser;
    }

    @Override
    public User getUserBy(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User getUserBy(long id) {
        return userRepository.findOne(id);
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
        User u = userRepository.findOne(user.getId());
        BeanUtils.copyProperties(user, u, Util.getNullPropertyNames(user));
        if (StringUtils.isNotEmpty(user.getHeadPic())) {
            Merchant merchant = merchantRepository.findByCreator(user.getId());
            if (merchant == null) return userRepository.save(u);
            merchant.setHeadPic(user.getHeadPic());
            merchantRepository.save(merchant);
        }
        return userRepository.save(u);
    }

    @Override
    public BankCard bindingBankCard(BankCard card) {
        BankCard c = bankCardRepository.findByUid(card.getUid());
        if (c != null) bankCardRepository.delete(c);
        return bankCardRepository.save(card);
    }

    @Override
    public BankCard getBindingBankCard(Long uid) {
        return bankCardRepository.findByUid(uid);
    }
}
