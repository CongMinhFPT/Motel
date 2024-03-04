package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.motel.entity.Tag;

public interface TagRepository extends JpaRepository<Tag,Integer>{

}