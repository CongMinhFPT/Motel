package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.motel.entity.MotelRoom;
import com.motel.entity.RoomCash;


public interface RoomCashRepository extends JpaRepository<RoomCash, Integer> {

    @Query("SELECT rc FROM RoomCash rc WHERE rc.motelRoom.motelRoomId = :motelRoomId")
    RoomCash findByMotelId(@Param("motelRoomId") Integer motelRoomId);
    
    @Query(nativeQuery = true, value = "select * from room_cash where motel_room_id = :motel_room_id")
    RoomCash findByMotelRoomId(@Param("motel_room_id") Integer motelRoomId);

}
