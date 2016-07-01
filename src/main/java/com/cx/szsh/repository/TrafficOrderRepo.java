package com.cx.szsh.repository;

import java.util.Date;

import org.springframework.data.mongodb.repository.Query;

import com.cx.szsh.models.TrafficOrder;

public interface TrafficOrderRepo extends BaseRepo<TrafficOrder, String> {
	@Query("{ 'account':?0, 'type':?1, 'status':?2, 'orderDate':{'$gte':?3} }")
	public TrafficOrder findValidOrder(String account, String type, String status, Date date);
	
	public TrafficOrder findByOrderId(String orderId);
}
