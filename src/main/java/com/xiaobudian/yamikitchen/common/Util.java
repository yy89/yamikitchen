package com.xiaobudian.yamikitchen.common;

/**
 * Created by Johnson on 2015/5/12.
 */
public final class Util {
    private static final String DISTANCE_PATTERN = "%.2f km";

    public static double calculateDistance(double r1, double r2, double t1, double t2) {
        return Math.pow(Math.abs(r1 - r2) % 360, 2) + Math.pow(Math.abs(t1 - t2) % 360, 2);
    }

    public static String calculateDistanceAsString(double r1, double r2, double t1, double t2) {
        return String.format(DISTANCE_PATTERN, calculateDistance(r1, r2, t1, t2));
    }
}
