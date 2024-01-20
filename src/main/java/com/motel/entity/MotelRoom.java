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
@Table(name = "MotelRoom")
public class MotelRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer motelRoomId;
    @Temporal(TemporalType.DATE)
    Date createDate = new Date();
    Double price;
    Double length;
    Double width;
    String video;
    @Nationalized
    String descriptions;
    boolean status = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
	List<Post> posts;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
	List<Motel> motel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
	List<Invoice> invoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
	List<Image> image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
	List<RoomRenter> roomRenter;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "motelRoom")
	List<FavoriteRoom> favoriteRoom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryRoomId")
    CategoryRoom categoryRoom;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cashId")
    Cash cash;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "indexId")
    Index index;
}   
