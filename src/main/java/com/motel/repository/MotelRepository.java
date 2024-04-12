package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Account;
import com.motel.entity.Motel;

public interface MotelRepository extends JpaRepository<Motel, Integer> {

    @Query(nativeQuery = true, value = "select * from motels a INNER JOIN motel_room b ON a.motel_id = b.motel_id where b.motel_room_id = :motel_room_id")
    List<Motel> statisticMotels(@Param("motel_room_id") Integer motelRoomId);

    @Query(nativeQuery = true, value = "select * from motels a LEFT JOIN motel_room b ON a.motel_id = b.motel_id WHERE b.motel_room_id NOT IN (SELECT motel_room_id FROM renter)")
    List<Motel> statisticMotelsNotInRenter();

    @Query(nativeQuery = true, value = "SELECT a.descriptions AS nha_tro, b.descriptions AS phong_tro, COUNT(c.motel_room_id) AS total_renter FROM motels a LEFT JOIN motel_room b ON a.motel_id = b.motel_id LEFT JOIN renter c ON b.motel_room_id= c.motel_room_id where a.account_id = :accountId GROUP BY a.descriptions, b.descriptions")
    List<Object> statisticMotelsRenter(@Param("accountId") Integer accountId);

    @Query(nativeQuery = true, value = "SELECT a.descriptions AS nha_tro, b.descriptions AS phong_tro, COUNT(c.motel_room_id) AS total_renter FROM motels a LEFT JOIN motel_room b ON a.motel_id = b.motel_id LEFT JOIN renter c ON b.motel_room_id= c.motel_room_id WHERE b.motel_room_id NOT IN (SELECT motel_room_id FROM renter) GROUP BY a.descriptions, b.descriptions")
    List<Object> statisticMotelsRenters();
}

