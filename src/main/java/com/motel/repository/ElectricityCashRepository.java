package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.ElectricityCash;

public interface ElectricityCashRepository extends JpaRepository<ElectricityCash, Integer>{
    @Query("SELECT rc FROM ElectricityCash rc WHERE rc.motelRoom.motelRoomId = :motelRoomId")
    ElectricityCash findByMotelIdOfElectricityCash(@Param("motelRoomId") Integer motelRoomId);

    @Query(nativeQuery = true, value = "select * from electricity_cash where motel_room_id = :motel_room_id")
    ElectricityCash findByMotelRoomId(@Param("motel_room_id") Integer motelRoomId);
}
