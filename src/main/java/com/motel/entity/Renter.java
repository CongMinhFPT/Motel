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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "renter")
// @ToString(exclude = { "invoice" })
public class Renter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer renterId;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date renterDate;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "renter")
    @JsonManagedReference
    List<Invoice> invoice;


    @ManyToOne
    @JoinColumn(name = "motelRoomId")
    @JsonBackReference

    MotelRoom motelRoom;

    @ManyToOne
    @JoinColumn(name = "accountId")
    @JsonIgnore
    Account account;

}
