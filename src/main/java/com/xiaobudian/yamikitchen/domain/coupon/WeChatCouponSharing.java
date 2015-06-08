package com.xiaobudian.yamikitchen.domain.coupon;

import com.xiaobudian.yamikitchen.common.Keys;
import com.xiaobudian.yamikitchen.common.Util;
import com.xiaobudian.yamikitchen.domain.member.WeChatUser;
import com.xiaobudian.yamikitchen.repository.RedisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Johnson on 2015/6/7.
 */
@Component(value = "weChatCouponSharing")
public class WeChatCouponSharing {
    private static final String ACCESS_TOKEN = "access_token";
    private static final String OPENID = "openid";
    private static final String EXPIRES_IN = "expires_in";
    private static final String TICKET = "expires_in";
    private static final String SIGNATURE_DELIMITER = "=";
    @Value(value = "${wechat.getAccessToken.url}")
    private String accessTokenUrl;
    @Value(value = "${wechat.getUserInfo}")
    private String getUserInfoUrl;
    @Value(value = "${coupon.redirect.url}")
    private String couponRedirectUrl;
    @Value(value = "${coupon.share.pattern}")
    private String urlPatternOfCouponShare;
    @Value(value = "${share.callback.url}")
    private String callbackUrl;
    @Value(value = "${wechat.access.token.url}")
    private String clientAccessTokenUrl;
    @Value(value = "${wechat.jsapi.ticket}")
    private String jsApiTicketUrl;
    @Value(value = "${wechat.signature.pattern}")
    private String signaturePattern;
    @Inject
    private RestTemplate restTemplate;
    @Inject
    private RedisRepository redisRepository;

    public String getShareUrl(String orderNo) {
        return MessageFormat.format(callbackUrl, orderNo);
    }

    public Principal getAccessToken(String code) {
        if (StringUtils.isEmpty(code)) return null;
        String url = MessageFormat.format(accessTokenUrl, code);
        Map<String, Object> r = Util.json2Map(restTemplate.getForObject(url, String.class));
        if (!r.containsKey(ACCESS_TOKEN) || !r.containsKey(OPENID)) return null;
        return new Principal(r.get(ACCESS_TOKEN).toString(), r.get(OPENID).toString());
    }

    public WeChatUser getUserInfo(Principal principal) {
        String url = MessageFormat.format(getUserInfoUrl, principal.getAccessToken(), principal.getOpenid());
        Map<String, Object> r = Util.json2Map(restTemplate.getForObject(url, String.class));
        if (!r.containsKey(OPENID)) return null;
        return new WeChatUser(principal.getOpenid(), r.get("nickname").toString(),
                r.get("province").toString(), r.get("city").toString(),
                r.get("country").toString(), r.get("headimgurl").toString());
    }

    public String getAuthorizeUrl(String orderNo) {
        return MessageFormat.format(urlPatternOfCouponShare, Util.encodeUrl(MessageFormat.format(callbackUrl, orderNo)));
    }

    public String getCouponRedirectUrl(WeChatUser user, String orderNo) {
        String htmlFileName = user.hasMobile() ? "hongbao.html" : "mobile.html";
        return MessageFormat.format(couponRedirectUrl, htmlFileName, orderNo, user.getOpenId());
    }

    public String getClientAccessTokenUrl() {
        if (StringUtils.isNotEmpty(redisRepository.get(Keys.weChatAccessToken())))
            return redisRepository.get(Keys.weChatAccessToken());
        Map<String, Object> r = Util.json2Map(restTemplate.getForObject(clientAccessTokenUrl, String.class));
        String token = r.get(ACCESS_TOKEN).toString();
        Long expireIn = Long.valueOf(r.get(EXPIRES_IN).toString());
        redisRepository.set(Keys.weChatAccessToken(), token, expireIn / 60);
        return token;
    }

    public String getJsApiTicket() {
        if (StringUtils.isNotEmpty(redisRepository.get(Keys.weChatJsApiTicket())))
            return redisRepository.get(Keys.weChatJsApiTicket());
        String url = MessageFormat.format(jsApiTicketUrl, getClientAccessTokenUrl());
        Map<String, Object> r = Util.json2Map(restTemplate.getForObject(url, String.class));
        String ticket = r.get(TICKET).toString();
        Long expireIn = Long.valueOf(r.get(EXPIRES_IN).toString());
        redisRepository.set(Keys.weChatAccessToken(), ticket, expireIn / 60);
        return ticket;
    }

    public Signature getSignature(String orderNo) {
        long t = new Date().getTime();
        String signature = MessageFormat.format(signaturePattern, getJsApiTicket(), t, orderNo);
        String[] ps = signature.split(SIGNATURE_DELIMITER);
        return new Signature(ps[2], t, ps[4], DigestUtils.sha1DigestAsHex(signature));
    }

    public static final class Principal {
        private final String accessToken;
        private final String openid;

        public Principal(String accessToken, String openid) {
            this.accessToken = accessToken;
            this.openid = openid;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getOpenid() {
            return openid;
        }
    }

    public static final class Signature {
        private String noncestr;
        private long timestamp;
        private String url;
        private String signature;

        public Signature() {
        }

        public Signature(String noncestr, long timestamp, String url, String signature) {
            this();
            this.noncestr = noncestr;
            this.timestamp = timestamp;
            this.url = url;
            this.signature = signature;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
