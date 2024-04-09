package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.RoomStatus;

public interface RoomStatusRepository extends JpaRepository<RoomStatus, Integer>  {

    
} 
