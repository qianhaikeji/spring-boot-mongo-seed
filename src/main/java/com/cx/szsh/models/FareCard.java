package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class FareCard {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String phone;
    private String identityCard;
    private String birthday;
    private String resident;
    private String addr;
    private String avatar;
    private Boolean disabled;
    private String type;

    public FareCard() {

    }

    public FareCard(String name, String phone, String identityCard, String birthday, String resident, String addr,
            String avatar, Boolean passed, Boolean disabled, String type) {
        super();
        this.name = name;
        this.phone = phone;
        this.identityCard = identityCard;
        this.birthday = birthday;
        this.resident = resident;
        this.addr = addr;
        this.avatar = avatar;
        this.disabled = disabled;
        this.type = type;
    }
}
