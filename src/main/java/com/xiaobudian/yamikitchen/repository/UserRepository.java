package com.xiaobudian.yamikitchen.repository;

import com.xiaobudian.yamikitchen.domain.User;
import org.hibernate.validator.constraints.Mod10Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


/**
 * Created by Johnson on 2015/4/22.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);

}
