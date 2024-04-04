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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favoriteRoom")
public class FavoriteRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer favoriteRoomId;

    @Temporal(TemporalType.DATE)
    Date createDate = new Date();
    boolean status;

    @ManyToOne
    @JoinColumn(name = "motelRoomId")

    MotelRoom motelRoom;

    @ManyToOne
    @JoinColumn(name = "accountId")

    Account account;
}
