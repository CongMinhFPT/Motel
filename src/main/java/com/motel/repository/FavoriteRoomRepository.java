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

    @Query(nativeQuery = true, value = "SELECT COUNT(*) AS total_favorites FROM motel_room m INNER JOIN favorite_room f ON m.motel_room_id = f.motel_room_id WHERE f.create_date = GETDATE()")
	Object findFavoriteToDay();

    @Query(nativeQuery = true, value = "SELECT m.descriptions, COUNT(*) AS total_favorites FROM motel_room m INNER JOIN favorite_room f ON m.motel_room_id = f.motel_room_id WHERE f.create_date = GETDATE() GROUP BY m.descriptions")
	List<Object> findFavoriteToDayMotelRoom();
}