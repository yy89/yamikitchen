package com.xiaobudian.yamikitchen.repository.member;

import com.xiaobudian.yamikitchen.domain.member.WeChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Johnson on 2015/6/1.
 */
public interface WeChatUserRepository extends JpaRepository<WeChatUser, Long> {
    public WeChatUser findByOpenId(String openId);

    public WeChatUser findByMobile(String mobile);
}
