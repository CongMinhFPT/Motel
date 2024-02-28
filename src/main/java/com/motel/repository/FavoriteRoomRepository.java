package com.motel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.FavoriteRoom;

public interface FavoriteRoomRepository extends JpaRepository<FavoriteRoom, Integer> {
    @Query(nativeQuery = true, value = "select * from favorite_room where motel_room_id = :motel_room_id")
    Page<FavoriteRoom> findByFavoriteRoom(@Param("motel_room_id") Integer motelRoomId, Pageable pageable);
}