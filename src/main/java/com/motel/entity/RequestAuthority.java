package com.motel.entity;

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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requestAuthority")
public class RequestAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer requestAuthorityId;
    String descriptions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "requestAuthority")
	List<RequestAuthorityStatus> requestAuthorityStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    @JsonBackReference
    Account account;


}
