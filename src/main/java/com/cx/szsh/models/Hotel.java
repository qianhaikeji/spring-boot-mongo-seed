package com.cx.szsh.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Hotel {
	@Id
	private String id;
	private String name;
	private String addr;
	private String phone;
	private String intro;
	private String cover;
	private String[] images;
	private String arriveTime;
	private String leaveTime;
	private String minPrice;
	private String type;
	private boolean haveWifi;
	private boolean havePark;
	private boolean haveGym;
	private boolean haveHotWater;
	private boolean haveBlower;
	private boolean haveTV;
	private List<HotelRoom> rooms;
	
	public Hotel(){
		
	}

	public Hotel(String name, String addr, String phone, String intro, String cover, String[] images, String arriveTime,
			String leaveTime, String minPrice, String type, boolean haveWifi, boolean havePark, boolean haveGym,
			boolean haveHotWater, boolean haveBlower, boolean haveTV, List<HotelRoom> rooms) {
		super();
		this.name = name;
		this.addr = addr;
		this.phone = phone;
		this.intro = intro;
		this.cover = cover;
		this.images = images;
		this.arriveTime = arriveTime;
		this.leaveTime = leaveTime;
		this.minPrice = minPrice;
		this.type = type;
		this.haveWifi = haveWifi;
		this.havePark = havePark;
		this.haveGym = haveGym;
		this.haveHotWater = haveHotWater;
		this.haveBlower = haveBlower;
		this.haveTV = haveTV;
		this.rooms = rooms;
	}
	
	public void addRoom(HotelRoom room) {
		if (this.rooms == null){
			this.rooms = new ArrayList<HotelRoom>();
		}
		this.rooms.add(room);
	}
}
