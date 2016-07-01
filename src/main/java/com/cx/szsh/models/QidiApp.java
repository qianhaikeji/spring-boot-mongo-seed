package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class QidiApp {
	@Id
	private String id;
	private String name;
	private String key;
	private String secret;
	
	public QidiApp(){
		
	}
	
	public QidiApp(String name, String key, String secret) {
		super();
		this.name = name;
		this.key = key;
		this.secret = secret;
	}
}
