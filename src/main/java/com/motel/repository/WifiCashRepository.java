package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.CategoryRoom;
import com.motel.entity.WifiCash;

public interface WifiCashRepository extends JpaRepository<WifiCash, Integer>{
    
}
