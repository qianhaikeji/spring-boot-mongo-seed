package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class HospitalOrder {
	@Id
	private String id;
	private String orderId;
	private String account;
	private String username;
	private String identityCard;
	private String phone;
	private String sex;
	private String bloodType;
	private String birthday;
	private String addr;
	private String type;
	private String status;
	private String hospitalId;
	private String departmentId;
	@Transient
	private String hospitalName;
	@Transient
	private String departmentName;
	@CreatedDate
	private Date createdTime;
	@JsonFormat(timezone="GMT+8")
	private Date orderDate;
	private String orderTime;
	
	public HospitalOrder() {
		
	}
	
	public HospitalOrder(String orderId, String account, String username, String identityCard, String phone, String sex,
			String bloodType, String birthday, String addr, String type, String status, String hospitalId,
			String departmentId, Date orderDate, String orderTime) {
		super();
		this.orderId = orderId;
		this.account = account;
		this.username = username;
		this.identityCard = identityCard;
		this.phone = phone;
		this.sex = sex;
		this.bloodType = bloodType;
		this.birthday = birthday;
		this.addr = addr;
		this.type = type;
		this.status = status;
		this.hospitalId = hospitalId;
		this.departmentId = departmentId;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
	}
}
