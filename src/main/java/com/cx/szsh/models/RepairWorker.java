package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class RepairWorker {
	@Id
	private String id;
	private String name;
	private String phone;
	private String avatar;
	private String type;
	private int orderCount;
	
	public RepairWorker(){
		
	}
	
	public RepairWorker(String id, String name, String phone, String avatar, String type, int orderCount) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.avatar = avatar;
		this.type = type;
		this.orderCount = orderCount;
	}
}
