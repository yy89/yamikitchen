package com.xiaobudian.yamikitchen.web.dto;

import com.xiaobudian.yamikitchen.domain.User;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Johnson on 2015/4/23.
 */
public class UserRequest {
    private String mobile;
    private String password;
    private String certCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertCode() {
        return certCode;
    }

    public void setCertCode(String certCode) {
        this.certCode = certCode;
    }
}
