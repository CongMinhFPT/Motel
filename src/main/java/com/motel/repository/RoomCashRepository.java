package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.MotelRoom;
import com.motel.entity.RoomCash;

public interface RoomCashRepository  extends JpaRepository<RoomCash, Integer>{
    
}
