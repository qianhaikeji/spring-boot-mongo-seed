package com.cx.szsh.auth;

import java.io.Serializable;

import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QidiAuthReq implements Serializable {

	private static final long serialVersionUID = 1260552114190382397L;
	
	@QueryParam("ssoTicket")
	private String token;
	@QueryParam("appKey")
	private String appKey;
	@QueryParam("appSecret")
	private String appSecret;
	@QueryParam("uri")
	private String uri;
	
	public QidiAuthReq() {
		
	}
	
	public QidiAuthReq(String token, String appKey, String appSecret, String uri) {
		super();
		this.token = token;
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.uri = uri;
	}
}
