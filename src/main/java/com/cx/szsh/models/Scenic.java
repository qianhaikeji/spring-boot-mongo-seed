package com.cx.szsh.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class Scenic {
    @Id
    private String id;
    @Indexed(unique=true)
    private String name;
    private int referencePrice;
    private int evaluation;
    private String addr;
    private String photo;
    private String externalData;
    private String capacity;
    private String intro;

    public Scenic(){
        
    }
    
    public Scenic(String name, int referencePrice, int evaluation, String addr, String photo, String externalData,
            String capacity, String intro) {
        super();
        this.name = name;
        this.referencePrice = referencePrice;
        this.evaluation = evaluation;
        this.addr = addr;
        this.photo = photo;
        this.externalData = externalData;
        this.capacity = capacity;
        this.intro = intro;
    }
}
