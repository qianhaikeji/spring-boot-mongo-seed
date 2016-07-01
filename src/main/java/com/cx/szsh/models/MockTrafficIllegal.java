package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class MockTrafficIllegal {
	private String title;
	private String money;
	private String point;
	@JsonFormat(timezone="GMT+8")
	private Date date;
	private String addr;
	
	public MockTrafficIllegal() {

	}
	
	public MockTrafficIllegal(String title, String money, String point, Date date, String addr) {
		super();
		this.title = title;
		this.money = money;
		this.point = point;
		this.date = date;
		this.addr = addr;
	}
}
