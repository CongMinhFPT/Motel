package com.motel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Account;
import com.motel.entity.FavoriteRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;

public interface RenterRepository extends JpaRepository<Renter, Integer> {
    @Query("SELECT r FROM Renter r WHERE r.account = :account")
    Renter findByAccount(Account account);

    @Query(nativeQuery = true, value = "select Count(*) AS total from renter")
    List<Object> statisticCountRenter();

    @Query("SELECT COUNT(r) FROM Renter r WHERE r.motelRoom = 1")
    long countByMotelRoom(MotelRoom motelRoom);

    @Modifying
    @Query(nativeQuery = true, value = "delete a from renter a where a.motel_room_id = :motel_room_id")
    void deleteRenterByMotelRoom(@Param("motel_room_id") Integer motelRoomId);

    @Query(nativeQuery = true, value = "select * from renter where motel_room_id =:motel_room_id")
    List<Renter> findRenterByMotelRoom(@Param("motel_room_id") Integer motelRoomId);
}
