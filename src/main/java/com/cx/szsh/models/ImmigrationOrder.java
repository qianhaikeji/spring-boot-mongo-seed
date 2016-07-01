package com.cx.szsh.models;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class ImmigrationOrder {
    @Id
    private String id;
    private String orderId;
    private String status;
    private String account;
    private String credentialType;
    private String applyType;
    private String username;
    private String nation;
    private String birthday;
    private String phone;
    private String identityCard;
    private String purpose;
    private String resident;
    private String originalCredential;
    @JsonFormat(timezone="GMT+8")
    private Date orderDate;
    private String orderTime;
    @CreatedDate
    private Date createdTime;
    
    public ImmigrationOrder() {
    }
    
    public ImmigrationOrder(String orderId, String account, String credentialName, String applyType, String username, String nation,
            String birthday, String phone, String identityCard, String purpose, String resident,
            String originalCredential, Date orderDate, String orderTime, Date createdTime) {
        super();
        this.orderId = orderId;
        this.account = account;
        this.credentialType = credentialName;
        this.applyType = applyType;
        this.username = username;
        this.nation = nation;
        this.birthday = birthday;
        this.phone = phone;
        this.identityCard = identityCard;
        this.purpose = purpose;
        this.resident = resident;
        this.originalCredential = originalCredential;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.createdTime = createdTime;
    }
}
