package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class MockInvoice {
	@Id
	private String id;
	private String code;
    @Indexed(unique=true)
	private String number;
	private String type;
	private String location;
	private String payer;
	private String payee;
	private String money;
	private String status;
	
	public MockInvoice() {
		
	}

	public MockInvoice(String code, String number, String type, String location, String payer, String payee,
			String money, String status) {
		super();
		this.code = code;
		this.number = number;
		this.type = type;
		this.location = location;
		this.payer = payer;
		this.payee = payee;
		this.money = money;
		this.status = status;
	}
	
}
