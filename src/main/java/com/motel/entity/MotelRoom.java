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
import javax.validation.constraints.Min;
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
@Table(name = "MotelRoom")
public class MotelRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer motelRoomId;
  @Temporal(TemporalType.DATE)
  Date createDate = new Date();
  @NotNull(message = "Vui lòng nhập chiều dài của phòng")
  @Min(value = 1, message = "Vui lòng nhập chiều dài của phòng là số dương")
  Double length;
  @NotNull(message = "Vui lòng nhập chiều rộng của phòng")
  @Min(value = 1, message = "Vui lòng nhập chiều rộng của phòng là số dương")
  Double width;
  String video;
  @Nationalized
  @NotEmpty(message = "Vui lòng nhập mô tả")
  String descriptions;
  boolean status = true;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonManagedReference
  List<Image> image;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonIgnore
  List<FavoriteRoom> favoriteRoom;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonManagedReference
  List<ElectricityCash> electricityCash;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonManagedReference
  List<WaterCash> waterCash;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonManagedReference
  List<WifiCash> wifiCash;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonManagedReference
  List<RoomCash> roomCash;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonIgnore
  List<Indexs> index;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "motelRoom")
  @JsonManagedReference
  List<Renter> renter;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "motelId")
  Motel motel;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "categoryRoomId")
  CategoryRoom categoryRoom;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "roomStatusId")
  RoomStatus roomStatus;

  @Override
  public String toString() {
    return "MotelRoom{" +
        "motelRoomId=" + motelRoomId +
        ", createDate=" + createDate +
        ", length=" + length +
        ", width=" + width +
        ", video='" + video + '\'' +
        ", descriptions='" + descriptions + '\'' +
        ", status=" + status +
        '}';
  }

  @Transient
  public String getAcreage() {
    double result = width * length;
    double render = Math.round(result * 10.0) / 10.0;
    String fomated = String.format("%.1f", render);
    return fomated;
  }

  @Transient
  public long countRenter() {
    if (renter == null) {
      return 0;
    }
    return renter.size();
  }

  @Transient
  public String getWifiCashTotal() {
    double totalWifiBill = 0.0;
    if (wifiCash != null) {
      for (WifiCash cash : wifiCash) {
        totalWifiBill += cash.getWifiBill();
      }
    }
    if (totalWifiBill == 0.0 || waterCash == null) {
      return "Miễn phí";
    }
    return String.format("%.1f", totalWifiBill);
  }

}
