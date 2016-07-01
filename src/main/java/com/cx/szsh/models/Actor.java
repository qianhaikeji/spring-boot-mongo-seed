package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Actor {
	@Id
	private String id;
	@DBRef
	private Activity activity;
	private String account;
	private String name;
	private String phone;
	private String identityCard;
	@CreatedDate
	private Date createdTime;
	
	public Actor() {
	}
	
	public Actor(String account, String name, String phone, String identityCard) {
		super();
		this.account = account;
		this.name = name;
		this.phone = phone;
		this.identityCard = identityCard;
	}
}
