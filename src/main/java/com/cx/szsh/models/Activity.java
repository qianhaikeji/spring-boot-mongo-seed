package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Activity {
	@Id
	private String id;
	private String title;
	private String content;
	private Date activeStartDate;
	private Date activeEndDate;
	private Date applyStartDate;
	private Date applyEndDate;
	private String addr;
	private String cover;
	private String org;
	private String orgBrief;
	private String contact;
	private String phone;
	private int maxNum;
	@Transient
	private int curNum;
	@CreatedDate
	private Date createdTime;
	
	public Activity() {

	}
	
	public Activity(String title, String content, Date activeStartDate, Date activeEndDate, Date applyStartDate,
			Date applyEndDate, String addr, String cover, String org, String orgBrief, String contact, String phone,
			int maxNum) {
		super();
		this.title = title;
		this.content = content;
		this.activeStartDate = activeStartDate;
		this.activeEndDate = activeEndDate;
		this.applyStartDate = applyStartDate;
		this.applyEndDate = applyEndDate;
		this.addr = addr;
		this.cover = cover;
		this.org = org;
		this.orgBrief = orgBrief;
		this.contact = contact;
		this.phone = phone;
		this.maxNum = maxNum;
	}
}
