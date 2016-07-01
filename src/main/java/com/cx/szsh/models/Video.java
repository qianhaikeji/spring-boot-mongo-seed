package com.cx.szsh.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Video {
	@Id
	private String id;
	@Indexed(unique=true)
	private String title;
	private String cover;
	private String location;
	private String notice;
	private String type;
	private String url;
	@JsonFormat(timezone="GMT+8")
	private Date startTime;
	@JsonFormat(timezone="GMT+8")
	private Date endTime;
	
    public Video() {

    }

	public Video(String title, String cover, String location, String notice, String type, String url, Date startTime, Date endTime) {
		super();
		this.title = title;
		this.cover = cover;
		this.location = location;
		this.notice = notice;
		this.type = type;
		this.url = url;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
