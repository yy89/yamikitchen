package com.xiaobudian.yamikitchen.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Johnson on 2015/5/12.
 */
public final class Util {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final double PI = Math.PI / 180.0;
    private static final double EARTH_RADIUS = 6378.137;

    public static double distanceBetween(double r1, double r2, double t1, double t2) {
        double a = (t1 - t2) * PI;
        double b = (r1 - r2) * PI;
        double s = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(t1 * PI) * Math.cos(t2 * PI) * Math.pow(Math.sin(b / 2), 2)));
        return Math.round(s * 1000);
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

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static Map<String, Object> json2Map(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            return null;
        }
    }
}
