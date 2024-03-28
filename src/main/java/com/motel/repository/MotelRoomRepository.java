package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.motel.entity.MotelRoom;

public interface MotelRoomRepository extends JpaRepository<MotelRoom, Integer> {

	@Query("SELECT mr FROM MotelRoom mr WHERE mr.motel.motelId = :motelId")
	List<MotelRoom> findByMotelId(Integer motelId);

}
