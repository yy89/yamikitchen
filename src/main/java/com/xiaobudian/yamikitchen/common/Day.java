package com.xiaobudian.yamikitchen.common;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Johnson on 2015/5/13.
 */
public final class Day {
    public static final Day TODAY = new Day(new Date());
    private final DateTime date;

    public Day(Date date) {
        this.date = new DateTime(date);
    }

    public Date startOfDay() {
        return date.withTimeAtStartOfDay().toDate();
    }

    public Date endOfDay() {
        return date.plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate();
    }

    public Date plusDays(int days) {
        return date.plusDays(days).toDate();
    }

    public Date getDate() {
        return date.toDate();
    }
}
