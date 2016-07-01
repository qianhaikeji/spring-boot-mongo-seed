package com.cx.szsh.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;

import com.cx.szsh.models.Actor;

public interface ActorRepo extends BaseRepo<Actor, String> {
	@Query(fields="{'activity':0}")
	public List<Actor> findByActivityId(String id);
	public int countByActivityId(String id);
}
