package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.motel.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

//    @Query(value = "Select * form Blog order by id desc limit :limit", nativeQuery = true)
//    List<Blog> getListNewest(int limit);
	
}