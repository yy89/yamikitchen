package com.xiaobudian.yamikitchen.common;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashSet;
import java.util.Set;

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

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper beanMapper = new BeanWrapperImpl(source);
        Set<String> result = new HashSet<>();
        for (PropertyDescriptor pd : beanMapper.getPropertyDescriptors()) {
            if (beanMapper.getPropertyValue(pd.getName()) == null) result.add(pd.getName());
        }
        return result.toArray(new String[result.size()]);
    }

    public static String signContent(String content, String privateKey, String charset) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            String sign = Base64.encodeBase64String(signature.sign());
            return URLEncoder.encode(sign, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }
}
