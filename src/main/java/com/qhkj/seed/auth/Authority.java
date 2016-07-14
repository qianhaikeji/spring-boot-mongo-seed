package com.qhkj.seed.auth;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Authority {
	public enum AuthorityName { 
	    ROLE_ADMIN,
	    ROLE_MEDICAL,
	    ROLE_CONVENIENCE,
	    ROLE_TRAFFIC,
	    ROLE_LIVEVIDEO,
	    ROLE_TOUR
	}
	
	private AuthorityName name;
	
	public Authority() {

	}
	
	public Authority(AuthorityName name) {
		super();
		this.name = name;
	}
}
