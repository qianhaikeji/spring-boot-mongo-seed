package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Food {
	@Id
	private String id;
	private String name;
	private String image;
	private String intro;
	
	public Food(){
		
	}
	
	public Food(String name, String image, String intro) {
		super();
		this.name = name;
		this.image = image;
		this.intro = intro;
	}
}
