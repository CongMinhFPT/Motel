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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "motels")
public class Motel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer motelId;
    @Nationalized
    String descriptions;
    @Nationalized
    String province;
    @Nationalized
    String district;
    @Nationalized
    String ward;
    @Nationalized
    String detailAddress;
    String image;
    @Temporal(TemporalType.DATE)
    Date createDate = new Date();
    boolean status = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motel")
    @JsonManagedReference
	List<MotelRoom> motelRoom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    @JsonBackReference
    Account account;

    @Override
public String toString() {
    return "Motel{" +
            "motelId=" + motelId +
            ", descriptions='" + descriptions + '\'' +
            ", province='" + province + '\'' +
            ", district='" + district + '\'' +
            ", ward='" + ward + '\'' +
            ", detailAddress='" + detailAddress + '\'' +
            ", image='" + image + '\'' +
            ", createDate=" + createDate +
            ", status=" + status +
            '}';
}
}
