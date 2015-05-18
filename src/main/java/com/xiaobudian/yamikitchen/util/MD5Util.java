package com.xiaobudian.yamikitchen.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5帮助类
 * @author Liuminglu
 * @Date 2015年5月15日 下午1:48:43
 */
public class MD5Util {
	
	/**
	 * MD5加密
	 * @param plainText 明文
	 * @return 32位密文
	 * @author Liuminglu
	 * @Date 2015年5月15日 下午1:48:43
	 */
	public static String md5(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
