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
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @NotEmpty(message = "Vui lòng nhập mô tả")
  @Nationalized
  String descriptions;
  @NotEmpty(message = "Vui lòng chọn tỉnh")
  @Nationalized
  String province;
  @NotEmpty(message = "Không được để trống mã tỉnh")
  String provinceID;
  @NotEmpty(message = "Vui lòng chọn huyện")
  @Nationalized
  String district;
  @NotEmpty(message = "Không được để trống mã huyện")
  String districtID;
  @NotEmpty(message = "Vui lòng chọn phường")
  @Nationalized
  String ward;
  @NotEmpty(message = "Vui lòng nhập số nhà")
  @Nationalized
  String detailAddress;
  String image;
  @Temporal(TemporalType.DATE)
  Date createDate = new Date();
  boolean status = true;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motel")
  @JsonIgnore
  List<MotelRoom> motelRoom;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motel")
  @JsonIgnore
  List<Post> posts;

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

  @Transient
  public String getAdressMotel() {
    return province + ", " + district;

  }
}
