package com.xiaobudian.yamikitchen.domain.message;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Johnson on 2015/1/27.
 */
@Entity
public class PushNotificationType implements Serializable {
    private static final long serialVersionUID = -3422242183824691077L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "[key]")
    private String key;
    private String pramName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = this.key;
    }

    public String getPramName() {
        return pramName;
    }

    public void setPramName(String pramName) {
        this.pramName = pramName;
    }
}
