package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Restaurant {
	@Id
	private String id;
	private String name;
	private String avatar;
	private String cover;
	private String type;
	private String addr;
	private String phone;
	private String brief;
	private String intro;
	
	public Restaurant(){
		
	}
	
	public Restaurant(String name, String avatar, String cover, String type, String addr, String phone, String brief, String intro) {
		super();
		this.name = name;
		this.avatar = avatar;
		this.cover = cover;
		this.type = type;
		this.addr = addr;
		this.phone = phone;
		this.brief = brief;
		this.intro = intro;
	}
}
