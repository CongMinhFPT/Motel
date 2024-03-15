package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.motel.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
	@Query("SELECT rc FROM Image rc WHERE rc.motelRoom.motelRoomId = :motelRoomId")
	Image findByImage_MotelRoomId(Integer motelRoomId);

	Iterable<Image> findByMotelRoom_MotelRoomId(Integer motelRoomId);

	@Query("SELECT i FROM Image i WHERE i.motelRoom.motelRoomId = :motelRoomId")
	List<Image> findImagesByMotelRoom_MotelRoomId(Integer motelRoomId);
}
