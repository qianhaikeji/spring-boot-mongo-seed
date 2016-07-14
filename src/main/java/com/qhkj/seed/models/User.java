package com.qhkj.seed.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.qhkj.seed.auth.Authority;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter @Setter
public class User {
    @Id
    private String id;
    @Indexed(unique=true)
    private String username;
    private String email;
    private String phone;
    @JsonProperty(access=Access.WRITE_ONLY)
    private String password;
    private String meta;
    private List<Authority> authorities;

    public User() {

    }

    public User(String username, String email, String phone, String password, String meta, List<Authority> authorities) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.meta = meta;
        this.authorities = authorities;
    }
}
