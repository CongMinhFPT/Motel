package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.motel.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
	
	@Modifying
	@Query(value = "DELETE FROM tag WHERE tag_id = :tagId", nativeQuery = true)
	void deleteByIdTag(Integer tagId);

}