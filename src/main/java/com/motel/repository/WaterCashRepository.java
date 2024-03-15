package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Indexs;
import com.motel.entity.WaterCash;

public interface WaterCashRepository extends JpaRepository<WaterCash, Integer>{
    @Query(nativeQuery = true, value = "select * from water_cash where motel_room_id = :motel_room_id")
    WaterCash findByMotelRoomId(@Param("motel_room_id") Integer motelRoomId);
}
