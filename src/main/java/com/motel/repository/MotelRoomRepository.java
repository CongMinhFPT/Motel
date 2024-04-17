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
import com.motel.entity.MotelRoom;

public interface MotelRoomRepository extends JpaRepository<MotelRoom, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM motel_room AS m inner join favorite_room a on m.motel_room_id = a.motel_room_id WHERE EXISTS (SELECT 1  FROM favorite_room AS f WHERE f.motel_room_id = m.motel_room_id)")
    List<MotelRoom> findMotelRoomByFavoriteRoom();

    @Query(nativeQuery = true, value = "select * from motel_room a inner join renter b on a.motel_room_id = b.motel_room_id")
    List<MotelRoom> findMotelRooms();

    List<MotelRoom> findByMotel_DistrictAndMotel_Province(String district, String province);

    @Modifying
    @Query(nativeQuery = true, value = "SELECT * FROM motel_room a LEFT JOIN indexs b ON a.motel_room_id = b.motel_room_id WHERE b.motel_room_id IS NULL")
    List<MotelRoom> findMotelRoomByIndex();

    @Query(nativeQuery = true, value = "SELECT mr.descriptions, COUNT(*) AS total_renters FROM motel_room mr INNER JOIN renter r ON mr.motel_room_id = r.motel_room_id GROUP BY mr.descriptions;")
    List<Object> statisticRenter();

    @Query(nativeQuery = true, value = "SELECT * FROM motel_room a INNER JOIN posts b ON a.motel_room_id = b.motel_room_id INNER JOIN category_room c ON a.category_room_id = c.category_room_id INNER JOIN motels d ON a.motel_id = d.motel_id")
    List<MotelRoom> findMotelRoomsByPost();

    @Query(nativeQuery = true, value = "select * from motel_room a inner join motels b on a.motel_id = b.motel_id where b.account_id = :accountId")
    List<MotelRoom> findMotelRoomsByAccount(@Param("accountId") Integer accountId);

    @Query(nativeQuery = true, value = "select * from motel_room order by createDate desc")
    List<MotelRoom> find3MotelRom(Pageable pageable);
}
