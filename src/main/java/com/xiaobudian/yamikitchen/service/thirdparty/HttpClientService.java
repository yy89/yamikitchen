package com.xiaobudian.yamikitchen.service.thirdparty;

import java.util.Map;

/**
 * @author Liuminglu 2015/5/19.
 */
public interface HttpClientService {

    String httpGet(String getUrl);

    String httpPost(String postUrl, Map<String, String> requestParams);

}
