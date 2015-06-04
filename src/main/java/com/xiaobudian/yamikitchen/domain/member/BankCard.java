package com.xiaobudian.yamikitchen.domain.member;

import com.xiaobudian.yamikitchen.thirdparty.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Johnson on 2015/5/20.
 */
@Entity
public class BankCard implements Serializable {
    private static final long serialVersionUID = 6889381147278000435L;
    private static final String FOUR_START = "****";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long uid;
    private String bankName;
    private String binCode;
    private String cardNo;
    private String branch;
    private String name;
    private Integer idType = 0;//���֤����
    private String idNo;
    private String region;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public String getCardNo() {
        String searchString = cardNo.substring(cardNo.length() - 3, cardNo.length());
        return StringUtils.replace(cardNo, searchString, FOUR_START);
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        String searchString = idNo.substring(idNo.length() - 3, idNo.length());
        return StringUtils.replace(idNo, searchString, FOUR_START);
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public boolean isValidIdNo() {
        return idNo != null && ValidateUtil.checkIdCard(idNo);
    }

    public boolean isValidCardNo() {
        return cardNo != null && ValidateUtil.checkBankCard(cardNo);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
