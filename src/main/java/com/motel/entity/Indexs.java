package com.motel.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "indexs")
public class Indexs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer indexId;
    Double electricityIndex;
    Double waterIndex;
    @Temporal(TemporalType.TIMESTAMP)
    Date createDate = new Date();
    @ManyToOne
    @JoinColumn(name = "motelRoomId")
    MotelRoom motelRoom;
}
