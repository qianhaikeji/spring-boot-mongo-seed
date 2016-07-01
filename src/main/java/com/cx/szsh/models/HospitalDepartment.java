package com.cx.szsh.models;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HospitalDepartment {
	private String id;
	private String name;
	private int normalNum;
	private int expertNum;
	
	public HospitalDepartment() {
		this.id = ObjectId.get().toString();
	}

	public HospitalDepartment(String name, int normalNum, int expertNum) {
		super();
		this.name = name;
		this.normalNum = normalNum;
		this.expertNum = expertNum;
	}
}
