package com.motel.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roomRenter")
public class RoomRenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer roomRenterId;
    @Nationalized
    String fullname;
    String phone;
    boolean gender;
    @Temporal(TemporalType.DATE)
    Date createDate = new Date();
    @Temporal(TemporalType.DATE)
    Date birthday;
    String identification;
    boolean status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motelRoom")
    MotelRoom motelRoom;
}
