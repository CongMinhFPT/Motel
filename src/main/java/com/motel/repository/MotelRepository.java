package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.Account;
import com.motel.entity.Motel;

public interface MotelRepository extends JpaRepository<Motel, Integer> {

    
} 
