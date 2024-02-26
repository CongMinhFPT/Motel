package com.motel.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User{
    private   Integer accountid;
    private   Integer motelid;
      public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities ,Integer accountid,Integer motelid) {
        super(username, password, authorities);
        this.accountid=accountid;
        this.motelid=motelid;
    }
    public Integer getAccountid(){
        return this.accountid;
    }
    public Integer getMotelid(){
        return this.motelid;
    }
  

    
}
