package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.RoomCash;

public interface RoomCashRepository extends JpaRepository<RoomCash, Integer> {

    @Query("SELECT rc FROM RoomCash rc WHERE rc.motelRoom.motelRoomId = :motelRoomId")
    RoomCash findByMotelId(@Param("motelRoomId") Integer motelRoomId);
}
