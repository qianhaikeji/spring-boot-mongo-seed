package com.qhkj.seed.utils;

import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;  
import org.apache.http.NameValuePair;  
import org.apache.http.ParseException;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;   
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.core.MediaType;

public class HttpClientHelper extends BaseHelper {
	
    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    public void post(String url) {
        // 创建默认的httpClient实例.  
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost  
        HttpPost httppost = new HttpPost(url);
        // 创建参数队列  
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("type", "house"));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            logger.info("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源  
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 get请求
     */
    public Object get(String url, Map<String, Object> reqParams) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        Object result = null;
        try {
            HttpGet httpget = new HttpGet(genGetMethodUrl(url, reqParams));
            logger.info("executing request " + httpget.getURI());
            
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                // 打印响应状态  
                // System.out.println(response.getStatusLine());
                if (entity != null) {
                    logger.info("Response content length: " + entity.getContentLength());
                    // System.out.println("Response content: " + EntityUtils.toString(entity));
                    if (entity.getContentType().getValue().startsWith(MediaType.TEXT_HTML)){
                        result = EntityUtils.toString(entity);
                    } else if (entity.getContentType().getValue().startsWith(MediaType.APPLICATION_JSON)){
                    	ObjectMapper mapper = new ObjectMapper();
                        String jsonResult = EntityUtils.toString(entity);
                        result = mapper.readValue(jsonResult, Object.class);
                    } 
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源  
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    private String genGetMethodUrl(String url, Map<String, Object> reqParams) {
    	if (reqParams != null){
    		int paramIndex = 0;
        	for (Map.Entry<String, Object> entry : reqParams.entrySet()) {
        		url += (paramIndex == 0) ? '?':'&';
        		url += entry.getKey() + '=' + entry.getValue();
        		paramIndex++;
        	}
    	}
    	
    	return url;
    }
}
