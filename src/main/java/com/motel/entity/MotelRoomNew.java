package com.motel.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
public class MotelRoomNew {
    Integer motelRoomId;
    Date createDate;
    Double length;
    Double width;
    String video;
    String descriptions;
    boolean status;
    List<String> Nameimg = new ArrayList<>();
    int Numberphotos;
    Double electricityBill;
    Double waterBill;
    Double wifiBill;
    Double roomBill;
    Integer People;
    Integer quantity;
    String name;

    public MotelRoomNew(MotelRoom motelRoom, List<String> Nameimg,
            Double electricityBill,
            Double waterBill,
            Double wifiBill,
            Double roomBill,
            Integer People,
            Integer quantity,
            String name) {
        this.motelRoomId = motelRoom.getMotelRoomId();
        this.createDate = motelRoom.getCreateDate();
        this.length = motelRoom.getLength();
        this.width = motelRoom.getWidth();
        this.video = motelRoom.getVideo();
        this.descriptions = motelRoom.getDescriptions();
        this.status = motelRoom.isStatus();
        this.Nameimg = Nameimg;
        this.Numberphotos = Nameimg.size();
        this.electricityBill = electricityBill;
        this.waterBill = waterBill;
        this.wifiBill = wifiBill;
        this.roomBill = roomBill;
        this.People = People;
        this.quantity = quantity;
        this.name = name;
    }
}
