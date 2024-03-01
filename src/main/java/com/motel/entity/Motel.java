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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Nationalized;
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
    @NotEmpty (message ="Không được để trống mô tả")
    @Nationalized
    String descriptions;
    @NotEmpty (message ="Không được để trống tỉnh")
    @Nationalized
    String province;
    @NotEmpty (message ="Không được để trống mã tỉnh")
    String provinceID;
    @NotEmpty (message ="Không được để trống huyện")
    @Nationalized
    String district;
    @NotEmpty (message ="Không được để trống mã huyện")
    String districtID;
    @NotEmpty(message ="Không được để trống phường")
    @Nationalized
    String ward;
    @NotEmpty (message ="Không được để trống số nhà")
    @Nationalized
    String detailAddress;
    String image;
    @Temporal(TemporalType.DATE)
    Date createDate = new Date();
    boolean status = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motel")
	List<MotelRoom> motelRoom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    Account account;
}
