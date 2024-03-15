package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.WaterCash;

public interface WaterCashRepository extends JpaRepository<WaterCash, Integer> {
    @Query("SELECT rc FROM WaterCash rc WHERE rc.motelRoom.motelRoomId = :motelRoomId")
    WaterCash findByMotelIdOfWaterCash(@Param("motelRoomId") Integer motelRoomId);
}
