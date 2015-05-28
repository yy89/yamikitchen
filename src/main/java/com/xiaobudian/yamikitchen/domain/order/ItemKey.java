package com.xiaobudian.yamikitchen.domain.order;

/**
 * Created by Johnson on 2015/5/12.
 */
public class ItemKey {
    private static final String DELIMITER = ":";
    private Long rid;
    private Long product;
    private Integer quality;

    public ItemKey(Long rid, Long product, Integer quality) {
        this.rid = rid;
        this.product = product;
        this.quality = quality;
    }

    public Long getRid() {
        return rid;
    }

    public Long getProduct() {
        return product;
    }

    public Integer getQuality() {
        return quality;
    }

    public static ItemKey valueOf(String s) {
        String[] ps = s.split(DELIMITER);
        return new ItemKey(Long.valueOf(ps[1]), Long.valueOf(ps[3]), Integer.valueOf(ps[5]));
    }
}
