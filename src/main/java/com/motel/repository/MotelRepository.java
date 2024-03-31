package com.motel.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.motel.entity.Motel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.Motel;

public interface MotelRepository extends JpaRepository<Motel, Integer>{
	Page<Motel> findAll(Pageable pageable);
}
