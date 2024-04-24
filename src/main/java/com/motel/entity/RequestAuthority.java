package com.motel.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @Nationalized
    @NotBlank(message =  "Vui lòng nhập yêu cầu!")
    String descriptions;
    
    @Nationalized
    String respdescriptions;
    
    String avatar;
    
    @NotBlank(message = "Vui lòng nhập mã số thuế!")
    String tax_code;
    
    @Temporal(TemporalType.DATE)
	@Column(name = "Createdate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	Date createDate = new Date();
    
    @Temporal(TemporalType.DATE)
 	@Column(name = "Responsedate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
 	Date responseDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requestAuthorityStatusId")
    RequestAuthorityStatus requestAuthorityStatus;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    Account account;


}
