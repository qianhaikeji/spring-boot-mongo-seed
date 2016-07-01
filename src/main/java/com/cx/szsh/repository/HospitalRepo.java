package com.cx.szsh.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.cx.szsh.models.Hospital;

public interface HospitalRepo extends BaseRepo<Hospital, String> {
	@Query(value="{ 'departments._id': ?0 }", fields="{'departments.$':1}")
	public Hospital findByDepartmentId(ObjectId id);
}
