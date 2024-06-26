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

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Nationalized
    String name;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "requestAuthorityStatus")
    @JsonIgnore
    List<RequestAuthority> requestAuthorities;
    
    @Override
	public String toString() {
	    return "RequestAuthorityStatus{" +
	            "requestAuthorityStatusId=" + requestAuthorityStatusId +
	            '}';
	}
}
