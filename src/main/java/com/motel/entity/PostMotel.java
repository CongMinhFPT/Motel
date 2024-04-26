package com.motel.entity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@AllArgsConstructor
public class PostMotel {
    Integer Postid;
    String Posttitle;
    Date PostcreateDate;
    Motel motel;
    Account account;
    Integer Availability;
    Integer Totalrooms;
    public PostMotel(Post post){
       this.Postid =post.getPostId();
       this.Posttitle = post.getTitle();
       this.PostcreateDate = post.getCreateDate();
       this.motel = post.getMotel();
       this.account = this.motel.getAccount();
       this.motel.setAccount(null);
       this.account.setPassword(null);
       this.account.setCitizen(null);
       this.Availability = getAvailabilityInteger(this.motel);
       this.Totalrooms = this.motel.getMotelRoom().size();
    }
    public Integer getAvailabilityInteger(Motel motel){
        List<MotelRoom> listmotelroom = motel.getMotelRoom();
          listmotelroom.stream().filter(motelRoom -> motelRoom.isStatus() && (motelRoom.getRenter() == null || motelRoom.getRenter().isEmpty()))
          .collect(Collectors.toList());
          return listmotelroom.size();
    }
    public long getDaysSinceCreation() {
        java.sql.Date sqlPostcreateDate = new java.sql.Date(PostcreateDate.getTime());
        LocalDate postDate = sqlPostcreateDate.toLocalDate();
        return ChronoUnit.DAYS.between(postDate, LocalDate.now());
    }
}

