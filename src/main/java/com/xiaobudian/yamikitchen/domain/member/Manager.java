package com.xiaobudian.yamikitchen.domain.member;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hackcoder on 2015/5/23.
 */
@Entity
@Table(name = "[Manager]")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Date createDate;
    private Date lastVisitedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastVisitedDate() {
        return lastVisitedDate;
    }

    public void setLastVisitedDate(Date lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }
}
