package com.xiaobudian.yamikitchen.domain.merchant;

import java.io.Serializable;

/**
 * Created by Johnson on 2015/4/25.
 */
public class FavoriteResult implements Serializable {
    private static final long serialVersionUID = -3964514922959640483L;
    private final Long rid;
    private final boolean favorited;

    public FavoriteResult(Long rid, boolean favorited) {
        this.rid = rid;
        this.favorited = favorited;
    }

    public Long getRid() {
        return rid;
    }

    public boolean isFavorited() {
        return favorited;
    }
}
