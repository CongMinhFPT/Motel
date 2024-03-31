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
    @Query(nativeQuery = true, value = "SELECT * FROM motel_room AS m inner join favorite_room a on m.motel_room_id = a.motel_room_id WHERE EXISTS (SELECT 1 FROM favorite_room AS f WHERE f.motel_room_id = m.motel_room_id)")
    List<MotelRoom> findMotelRoomByFavoriteRoom();

    @Query("SELECT mr FROM MotelRoom mr WHERE mr.motel.motelId = :motelId")
	List<MotelRoom> findByMotelId(Integer motelId);

    @Query(nativeQuery = true, value = "select * from motel_room a inner join renter b on a.motel_room_id = b.motel_room_id")
    List<MotelRoom> findMotelRooms();
    
    List<MotelRoom> findByMotel_DistrictAndMotel_Province(String district, String province);

    @Modifying
    @Query(nativeQuery = true, value = "SELECT * FROM motel_room a LEFT JOIN indexs b ON a.motel_room_id = b.motel_room_id WHERE b.motel_room_id IS NULL")
    List<MotelRoom> findMotelRoomByIndex();

}
