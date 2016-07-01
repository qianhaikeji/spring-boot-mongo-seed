package com.cx.szsh.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HotelRoom {
	private String id;
	private String name;
	private String cover;
	private String type;
	private String area;
	private String limit;
	private String floor;
	private String breadfast;
	private String bedSize;
	private String extraBed;
	private String internet;
	private String price;
	
	public HotelRoom(){
		this.id = ObjectId.get().toString();
	}
	
	public HotelRoom(String name, String cover, String type, String area, String limit, String floor, String breadfast,
			String bedSize, String extraBed, String internet, String price) {
		super();
		this.name = name;
		this.cover = cover;
		this.type = type;
		this.area = area;
		this.limit = limit;
		this.floor = floor;
		this.breadfast = breadfast;
		this.bedSize = bedSize;
		this.extraBed = extraBed;
		this.internet = internet;
		this.price = price;
	}
}
