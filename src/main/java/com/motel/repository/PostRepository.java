package com.motel.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.motel.entity.Post;
import com.motel.entity.RoomCash;

public interface PostRepository extends JpaRepository<Post, Integer> {
	 List<Post> findByMotel_DistrictAndMotel_Province(String
	 district, String province);

//	@Query(nativeQuery = true, value = "SELECT COUNT(*) AS NumberOfPosts FROM posts WHERE create_date = GETDATE() AND status = 1")
//	Object findPostToDay();
//
//	@Query(nativeQuery = true, value = "SELECT p.title, COUNT(*) AS NumberOfPosts, p.create_date FROM posts p INNER JOIN motel_room m ON p.motel_room_id = m.motel_room_id WHERE p.create_date = GETDATE() AND p.status = 1 GROUP BY p.title, p.create_date")
//	List<Object> findPostToDayMotelRoom();
//
//	@Query(nativeQuery = true, value = "SELECT * FROM posts WHERE status = 0")
//	List<Post> findPostByNonActive();
//
//	@Query(nativeQuery = true, value = "SELECT * FROM posts AS p WHERE EXISTS (SELECT 1  FROM favorite_room AS f WHERE f.motel_room_id = p.motel_room_id)")
//	List<Post> findPostByFavorite();
//
//	/*
//	 * @Query("SELECT TOP(3) p FROM Post p ORDER BY createDate DESC") List<Post>
//	 * find3PostFirst();
//	 */
//
//	@Query("SELECT p FROM Post p ORDER BY createDate DESC")
//	List<Post> find3PostFirst();

}
