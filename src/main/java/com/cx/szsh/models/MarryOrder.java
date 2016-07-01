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
public class MarryOrder {
    @Id
    private String id;
    private String orderId;
    private String account;
    private String husbandName;
    private String husbandPhone;
    private String husbandIdCard;
    private String husbandResdient;
    private String wifeName;
    private String wifePhone;
    private String wifeIdCard;
    private String wifeResdient;
    @JsonFormat(timezone="GMT+8")
    private Date orderDate;
    private String orderTime;
    private String status;
    @CreatedDate
    private Date createdTime;
    
    public MarryOrder() {

	}
    
	public MarryOrder(String orderId, String account, String husbandName, String husbandPhone, String husbandIdCard,
			String husbandResdient, String wifeName, String wifePhone, String wifeIdCard, String wifeResdient,
			Date orderDate, String orderTime, String status) {
		super();
		this.orderId = orderId;
		this.account = account;
		this.husbandName = husbandName;
		this.husbandPhone = husbandPhone;
		this.husbandIdCard = husbandIdCard;
		this.husbandResdient = husbandResdient;
		this.wifeName = wifeName;
		this.wifePhone = wifePhone;
		this.wifeIdCard = wifeIdCard;
		this.wifeResdient = wifeResdient;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
		this.status = status;
	}
}
