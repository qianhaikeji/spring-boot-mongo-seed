package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class MockTrafficPoint {
	@Id
	private String id;
	private String name;
	private String idCard;
	private String license;
	private String fn;
	private String leftPoint;
	private String totalMoney;
	private MockTrafficIllegal[] records;
	
	public MockTrafficPoint(){
		
	}
	
	public MockTrafficPoint(String name, String idCard, String license, String fn, String leftPoint, String totalMoney,
			MockTrafficIllegal[] records) {
		super();
		this.name = name;
		this.idCard = idCard;
		this.license = license;
		this.fn = fn;
		this.leftPoint = leftPoint;
		this.totalMoney = totalMoney;
		this.records = records;
	}	
}
