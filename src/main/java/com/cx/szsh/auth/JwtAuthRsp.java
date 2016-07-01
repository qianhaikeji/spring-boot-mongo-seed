package com.cx.szsh.auth;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@Getter
public class JwtAuthRsp implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String username;
    private final String token;
    private final String meta;
    private final List<Authority> authorities;

    public JwtAuthRsp(String username, String token, String meta, List<Authority> authorities) {
    	this.username = username;
        this.token = token;
        this.meta = meta;
        this.authorities = authorities;
    }
}
