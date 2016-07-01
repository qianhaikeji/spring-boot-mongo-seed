package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Hotline {
	@Id
	private String id;
	@Indexed(unique=true)
	private String name;
	private String addr;
	private String image;
	private String contact;
	private String fixPhone;
	private String phone;
	private String type;
	
	public Hotline() {
		
	}
	
	public Hotline(String name, String addr, String image, String contact, String fixPhone, String phone, String type) {
		super();
		this.name = name;
		this.addr = addr;
		this.image = image;
		this.contact = contact;
		this.fixPhone = fixPhone;
		this.phone = phone;
		this.type = type;
	}
}
