package com.motel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.motel.Until.page.SearchRepository;
import com.motel.entity.Blog;

public interface BlogRepository extends SearchRepository<Blog, Integer> {

	@Query("SELECT b FROM Blog b WHERE b.tag.tagId = ?1 ")
	public Page<Blog> findAllInBlog(Integer tagId, Pageable pageable);

	@Query("SELECT b FROM Blog b WHERE b.title LIKE %?1%")
	public Page<Blog> findAll(String keyword, Pageable pageable);

	@Query("SELECT b FROM Blog b WHERE (b.tag.tagId = ?1) AND"
			+ "(b.title LIKE %?2% OR "
			+ "b.createDate LIKE %?2%)")
	public Page<Blog> searchInTag(Integer tagId, String keyword, Pageable pageable);

	@Query("SELECT b FROM Blog b WHERE b.tag.tagId = ?1 ")
	public Page<Blog> findAllInTag(Integer tagId, Pageable pageable);

	@Modifying
	@Query(value = "DELETE FROM blog WHERE blog_id = :blogId", nativeQuery = true)
	void deleteByBlogId(@Param("blogId") Integer blogId);

	@Modifying
	@Query(value = "DELETE FROM blog WHERE tag_id = :tagId", nativeQuery = true)
	void deleteByTagId(@Param("tagId") Integer tagId);

	@Query("SELECT b FROM Blog b WHERE b.tag.tagId = ?1 ")
	public List<Blog> listSimilar(Integer tagId);
}