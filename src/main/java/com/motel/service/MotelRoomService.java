package com.motel.service;

import java.util.List;
import java.util.Optional;

import com.motel.entity.MotelRoom;

public interface MotelRoomService {
    List<MotelRoom> getAll();

    Optional<MotelRoom> getById(Integer motelRoomId);
}
