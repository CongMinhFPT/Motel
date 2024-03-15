package com.motel.entity;

import java.util.Date;

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
import javax.validation.constraints.DecimalMin;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "waterCash")
public class WaterCash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer waterCashId;
     @DecimalMin( value = "0.1",message = "loi loi loi 2") 
    Double waterBill;
    @Temporal(TemporalType.DATE)
    Date createDate = new Date();
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motelRoomId")
    @JsonBackReference
    MotelRoom motelRoom;
}
