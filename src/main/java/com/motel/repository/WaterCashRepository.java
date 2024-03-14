package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.CategoryRoom;
import com.motel.entity.WaterCash;

public interface WaterCashRepository extends JpaRepository<WaterCash, Integer>{

    
} 
