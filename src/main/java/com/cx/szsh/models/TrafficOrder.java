package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class TrafficOrder {
	@Id
	private String id;
	private String orderId;
	private String account;
	private String username;
	private String identityCard;
	private String phone;
	private String type;
	private String status;
	private String carProperty;
	private String plateNum;
	private String plateType;
	private String carType;
	private String lastNum;
	private String illegalCode;
	@CreatedDate
	private Date createdTime;
	@JsonFormat(timezone="GMT+8")
	private Date orderDate;
	private String orderTime;
	
	public TrafficOrder() {
		
	}

	public TrafficOrder(String orderId, String account, String username, String identityCard, String phone, String type, String status,
			String carProperty, String plateNum, String plateType, String carType, String lastNum, String illegalCode, Date orderDate,
			String orderTime) {
		super();
		this.orderId = orderId;
		this.account = account;
		this.username = username;
		this.identityCard = identityCard;
		this.phone = phone;
		this.type = type;
		this.status = status;
		this.carProperty = carProperty;
		this.plateNum = plateNum;
		this.plateType = plateType;
		this.carType = carType;
		this.lastNum = lastNum;
		this.illegalCode = illegalCode;
		this.orderDate = orderDate;
	    this.orderTime = orderTime;

	}
}
