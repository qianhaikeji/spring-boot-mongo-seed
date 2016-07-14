package com.qhkj.seed.utils;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Component;

@Component
public class SpiderHelper extends BaseHelper {
	private static final int GET_TIMEOUT = 60 * 1000; //单位 ms
	
    public Document getHtmlDocument(String url, Map<String, Object> params) throws IOException {
        url = genGetMethodUrl(url, params);
        logger.info(url);
        return Jsoup.connect(url).timeout(GET_TIMEOUT).get();
    }
    
    public Document getHtmlDocument(String url, Map<String, Object> params, Map<String, String> cookies) throws IOException {
        url = genGetMethodUrl(url, params);
        logger.info(url);
        return Jsoup.connect(url).cookies(cookies).timeout(GET_TIMEOUT).get();
    }
    
    public Document postXmlDocument(String url, Map<String, String> params) throws IOException {
    	  Response rsp = Jsoup.connect(url)
    			.header("Content-Type", "application/x-www-form-urlencoded")
    			.data(params).timeout(GET_TIMEOUT).method(Method.POST).execute();
    	  return Jsoup.parse(rsp.body(), "", Parser.xmlParser());
    }
    
    public Response getResponse(String url, Map<String, Object> reqParams) throws IOException {
        url = genGetMethodUrl(url, reqParams);
        logger.info(url);
        return Jsoup.connect(url).timeout(GET_TIMEOUT).execute();
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
