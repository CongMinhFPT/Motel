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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "renter")
public class Renter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer renterId;
    @Temporal(TemporalType.DATE)
    Date renterDate = new Date();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "renter")
	List<Invoice> invoice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motelRoomId")
    MotelRoom motelRoom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    Account account;

}
