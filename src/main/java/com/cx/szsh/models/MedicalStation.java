package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class MedicalStation {
	@Id
	private String id;
	@Indexed(unique=true)
	private String name;
	private String addr;
	private String phone;
	private String openingTime;
	private String service;
	private String image;
	
	public MedicalStation() {
		
	}

	public MedicalStation(String name, String addr, String phone, String openingTime, String service, String image) {
		super();
		this.name = name;
		this.addr = addr;
		this.phone = phone;
		this.openingTime = openingTime;
		this.service = service;
		this.image = image;
	}

}
