package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.Authority;
import com.motel.entity.CategoryRoom;

public interface CategoryRoomRepository extends JpaRepository<CategoryRoom, Integer>{
    
}
