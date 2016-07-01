package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class RepairOrder {
	@Id
	private String id;
	private String orderId;
	private String account;
	private String username;
	private String phone;
	private String type;
	private String status;
	private String addr;
	private String remark;
	@DBRef
	private RepairWorker worker;
	private int evaluation;
	@CreatedDate
	private Date createdTime;
	@JsonFormat(timezone="GMT+8")
	private Date orderDate;
	private String orderTime;
	
	public RepairOrder() {
		
	}
	
	public RepairOrder(String orderId, String account, String username, String phone, String type, String status, String addr,
			String remark, int evaluation, Date orderDate, String orderTime) {
		super();
		this.orderId = orderId;
		this.account = account;
		this.username = username;
		this.phone = phone;
		this.type = type;
		this.status = status;
		this.addr = addr;
		this.remark = remark;
		this.evaluation = evaluation;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
	}
}
