package com.cx.szsh.auth;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QidiAuthRsp implements Serializable {
	private final String username;
    private final String token;
    
	public QidiAuthRsp(String username, String token) {
		super();
		this.username = username;
		this.token = token;
	}
}
