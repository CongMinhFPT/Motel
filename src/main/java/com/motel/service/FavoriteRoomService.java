package com.motel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.motel.entity.FavoriteRoom;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;

public interface FavoriteRoomService {
    FavoriteRoom addRoom(Integer accountId, Integer motelRoomId);

    void deleteRoom(Integer favoriteRoomId);

    List<Post> getPagedFavoriteRooms();

    Page<FavoriteRoom> getAllFavoriteRoom(Pageable pageable);
}
