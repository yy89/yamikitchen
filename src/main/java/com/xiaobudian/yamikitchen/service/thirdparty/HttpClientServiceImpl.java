package com.xiaobudian.yamikitchen.service.thirdparty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;


/**
 * @author Liuminglu 2015/5/19.
 */
@SuppressWarnings("deprecation")
@Service(value = "httpclientService")
public class HttpClientServiceImpl implements HttpClientService {
	
	@Override
	public String httpGet(String getUrl) {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		try {
			// 创建httpget
			HttpGet httpget = new HttpGet(getUrl);
			// 执行get请求
			HttpResponse response = httpclient.execute(httpget);
			// 获取响应实体
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			return EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}
	
	@Override
	public String httpPost(String postUrl, Map<String, String> requestParams) {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		// 建立HttpPost对象
		HttpPost httppost = new HttpPost(postUrl);
		// 建立NameValuePair数组，用于存储请求的参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : requestParams.entrySet()) {  
            params.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));  
        }
		try {
			// 添加参数
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 设置编码
			HttpResponse response = httpclient.execute(httppost);
			// 发送Post并返回HttpResponse对象
			if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
		return null;
	}
}
