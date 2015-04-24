package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by Johnson on 2015/4/22.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
