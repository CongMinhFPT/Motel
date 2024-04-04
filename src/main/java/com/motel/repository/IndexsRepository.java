package com.motel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.FavoriteRoom;
import com.motel.entity.Indexs;

public interface IndexsRepository extends JpaRepository<Indexs, Integer> {
    @Query(nativeQuery = true, value = "select * from indexs where motel_room_id = :motel_room_id")
    List<Indexs> findByMotelRoomId(@Param("motel_room_id") Integer motelRoomId);

    @Query(nativeQuery = true, value = "SELECT * FROM indexs WHERE motel_room_id = :motel_room_id ORDER BY YEAR(create_date) DESC, MONTH(create_date) DESC")
    List<Indexs> findByMotelRoomIdOrderByMonth(@Param("motel_room_id") Integer motelRoomId);

    @Query(nativeQuery = true, value = "SELECT * FROM indexs ORDER BY YEAR(create_date) DESC, MONTH(create_date) DESC")
    List<Indexs> findAllDESC();

    @Modifying
    @Query(nativeQuery = true, value = "delete a from indexs a where a.motel_room_id = :motel_room_id")
    void deleteIndexByMotelRoom(@Param("motel_room_id") Integer motelRoomId);

}
