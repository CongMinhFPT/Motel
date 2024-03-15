package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.WaterCash;
import com.motel.entity.WifiCash;

public interface WifiCashRepository extends JpaRepository<WifiCash, Integer>{
    @Query(nativeQuery = true, value = "select * from wifi_cash where motel_room_id = :motel_room_id")
    WifiCash findByMotelRoomId(@Param("motel_room_id") Integer motelRoomId);
}
