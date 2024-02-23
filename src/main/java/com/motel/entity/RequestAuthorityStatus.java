package com.motel.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requestAuthorityStatus")
public class RequestAuthorityStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer requestAuthorityStatusId;
    String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requestAuthorityId")
    RequestAuthority requestAuthority;
    
}
