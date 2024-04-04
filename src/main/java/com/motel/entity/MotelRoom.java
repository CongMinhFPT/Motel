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
    Double length;
    Double width;
    String video;
    @Nationalized
    String descriptions;
    boolean status = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
    @JsonManagedReference
    List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
    @JsonManagedReference
    List<Image> image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
    @JsonManagedReference
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
    @JsonManagedReference
    List<Indexs> index;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "motelRoom")
    @JsonManagedReference
    List<Renter> renter;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motelId")
    @JsonBackReference
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
}
