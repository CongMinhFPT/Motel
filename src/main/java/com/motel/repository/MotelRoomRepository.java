package com.motel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.FavoriteRoom;
import com.motel.entity.MotelRoom;

public interface MotelRoomRepository extends JpaRepository<MotelRoom, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM motel_room AS m inner join favorite_room a on m.motel_room_id = a.motel_room_id WHERE EXISTS (SELECT 1 FROM favorite_room AS f WHERE f.motel_room_id = m.motel_room_id)")
    Page<MotelRoom> findMotelRoomByFavoriteRoom(Pageable pageable);
}
