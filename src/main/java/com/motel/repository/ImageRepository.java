package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.Image;

public interface ImageRepository extends JpaRepository <Image,Integer> {

    
}