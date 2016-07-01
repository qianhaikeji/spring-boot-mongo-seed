package com.cx.szsh.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Hospital {
	@Id
	private String id;
	@Indexed(unique=true)
	private String name;
	private String level;
	private String image;
	private String intro;
	private String phone;
	private String addr;
	private List<HospitalDepartment> departments;

    public Hospital () {
    	
    }
    
	public Hospital(String name, String level, String image, String intro, String phone, String addr) {
		super();
		this.name = name;
		this.level = level;
		this.image = image;
		this.intro = intro;
		this.phone = phone;
		this.addr = addr;
		this.departments = new ArrayList<HospitalDepartment>();
	}
	
	public void addDepartment(HospitalDepartment department){
		if (this.departments == null){
			this.departments = new ArrayList<HospitalDepartment>();
		}
		this.departments.add(department);
	}
}
