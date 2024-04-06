package com.motel.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.CategoryRoom;

public interface CategoryRoomRepository extends JpaRepository<CategoryRoom, Integer> {

	@Query("SELECT m FROM CategoryRoom m WHERE m.status = true")
	List<CategoryRoom> findActive();

	CategoryRoom getBytitle(String title);

	@Query("SELECT c FROM CategoryRoom c WHERE c.title LIKE %:keyword%")
	List<CategoryRoom> findByTitle(@Param("keyword") String keyword);

}
