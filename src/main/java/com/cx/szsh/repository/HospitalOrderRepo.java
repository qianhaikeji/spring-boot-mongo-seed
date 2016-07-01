package com.cx.szsh.repository;

import java.util.Date;

import org.springframework.data.mongodb.repository.Query;

import com.cx.szsh.models.HospitalOrder;

public interface HospitalOrderRepo extends BaseRepo<HospitalOrder, String> {
    @Query("{ 'account':?0, 'departmentId':?1, 'status':?2, 'orderDate':{'$gte':?3} }")
    public HospitalOrder findValidOrder(String account, String did, String status, Date date);
    public int countByHospitalIdAndDepartmentIdAndTypeAndOrderDate(String hospitalId, String departmentId, String type, Date orderDate);
}
