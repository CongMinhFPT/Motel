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

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer serviceId;

    @Nationalized
    String title;
    @Nationalized
    String description;
    @Temporal(TemporalType.DATE)
    Date timeStart = new Date();
    @Temporal(TemporalType.DATE)
    Date timeEnd = new Date();
    boolean status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "services")
	List<Account> account;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "serviceStatusId")
    ServiceStatus serviceStatus;
}
