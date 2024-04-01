package com.motel.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Nationalized;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categoryRoom")
public class CategoryRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer categoryRoomId;
    @NotBlank(message = "Tên tiêu đề không được bỏ trống")
    @Nationalized
    String title;
    @NotBlank(message = "Mô tả không được bỏ trống")
    @Nationalized
    String descriptions;
    boolean status=true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "categoryRoom")
	List<MotelRoom> motelRoom;
}
