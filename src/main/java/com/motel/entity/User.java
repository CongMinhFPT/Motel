package com.motel.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class User {
    @Id
    private String nickName;
    private String fullName;
    private String avatar;
    private Status status;
    public User() {
    }
    
    public User(String nickName) {
        this.nickName = nickName;
    }
}
