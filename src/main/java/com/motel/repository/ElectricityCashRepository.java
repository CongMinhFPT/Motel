package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.CategoryRoom;
import com.motel.entity.ElectricityCash;

public interface ElectricityCashRepository extends JpaRepository<ElectricityCash, Integer> {
    
}
