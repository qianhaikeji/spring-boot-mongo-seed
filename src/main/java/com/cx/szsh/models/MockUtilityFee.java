package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class MockUtilityFee {
	@Id
	private String id;
	private String account;
	private String type;
	private String data;
	
	public MockUtilityFee() {
		
	}
	
	public MockUtilityFee(String account, String type, String data) {
		super();
		this.account = account;
		this.type = type;
		this.data = data;
	}
}
