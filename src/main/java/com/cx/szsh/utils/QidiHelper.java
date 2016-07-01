package com.cx.szsh.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nqsky.meap.api.request.message.SendMessageRequest;
import com.nqsky.meap.api.request.sso.SsoRequest;
import com.nqsky.meap.api.response.StringResponse;
import com.nqsky.meap.api.response.userCenter.UserInfo;
import com.nqsky.meap.api.sdk.ClientBuilder;
import com.nqsky.meap.api.sdk.IClient;

@Component
public class QidiHelper {
	
    @Value("${qidi.auth.url}")
    private String authUrl;
	
    public UserInfo authenticate(String token, String appKey, String appSecret) {
    	IClient client = ClientBuilder.newBuilder().build(authUrl, appKey, appSecret);
		SsoRequest request = new SsoRequest();
		request.setSsoTicket(token);
		return client.execute(request);
    }
    
	public Boolean validateToken(String token, String appKey, String appSecret) {
		UserInfo rsp = authenticate(token, appKey, appSecret);
		return rsp.isSuccess();
    }

	public String pushNotifyMessage(String title, String summary, String appKey, String appSecret){
		IClient client = ClientBuilder.newBuilder().build(authUrl, appKey, appSecret);
        SendMessageRequest request = new SendMessageRequest();       
//        String title = "楚雄数字生活测试"; 
//        String summary = "测试推送";
        
        request.putCustomParam("tokenType", "ALL_USER");
        request.putCustomParam("tokens", "");
        request.putCustomParam("msgType", "text");
        //扩展参数,URL字符串,如果进入应用必须添加toApp=1的参数
        request.putCustomParam("exparams", "&toApp=1");
        request.putCustomParam("topic", appKey);
        request.putCustomParam("title", title);
        request.putCustomParam("summary", summary);
        request.putCustomParam("content", "");

        StringResponse response = client.execute(request);
        return response.getBody();
	}
}
