package com.xiaobudian.yamikitchen.domain.member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Johnson on 2015/5/13.
 */
@Entity
public class IdfaRecord implements Serializable {
    private static final long serialVersionUID = -6364210581355440008L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idfa;
    private Date createDate = new Date();

    public IdfaRecord() {
    }

    public IdfaRecord(String idfa) {
        this();
        this.idfa = idfa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
