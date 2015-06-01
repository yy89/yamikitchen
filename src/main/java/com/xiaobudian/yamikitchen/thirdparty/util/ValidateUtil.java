package com.xiaobudian.yamikitchen.thirdparty.util;

/**
 * Created by hackcoder on 2015/5/28.
 */
public class ValidateUtil {

    public static boolean checkIdCard(String idcard) {
        String patternCardno = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        return idcard != null && idcard.matches(patternCardno);
    }

    /**
     * У�����п�����
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * �Ӳ���У��λ�����п����Ų��� Luhm У���㷨���У��λ
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //���Ĳ�����ݷ���N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
