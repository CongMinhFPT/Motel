package com.motel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.motel.entity.Motel;
import com.motel.entity.Post;
import com.motel.entity.RoomCash;

public interface PostRepository extends JpaRepository<Post, Integer> {
	List<Post> findByMotel_DistrictAndMotel_Province(String district, String province);

	@Query("SELECT p FROM Post p JOIN p.motel m WHERE m.motelId = :motelId")
	Post findPostByMotelId(@Param("motelId") Integer motelId);

	@Query(nativeQuery = true, value = "SELECT COUNT(*) AS NumberOfPosts FROM posts WHERE create_date = CONVERT(DATE, GETDATE()) AND status = 1;")
	Object findPostToDay();

	@Query(nativeQuery = true, value = "SELECT p.title, COUNT(*) AS NumberOfPosts, p.create_date FROM posts p INNER JOIN motels m ON p.motel_id = m.motel_id WHERE p.create_date = CONVERT(DATE, GETDATE()) AND p.status = 1 GROUP BY p.title, p.create_date")
	List<Object> findPostToDayMotelRoom();

	@Query(nativeQuery = true, value = "SELECT * FROM posts WHERE status = 0")
	List<Post> findPostByNonActive();

	@Query(nativeQuery = true, value = "SELECT * FROM posts AS p WHERE EXISTS (SELECT 1  FROM favorite_room AS f WHERE f.motel_room_id = p.motel_room_id)")
	List<Post> findPostByFavorite();

	/*
	 * @Query("SELECT TOP(3) p FROM Post p ORDER BY createDate DESC") List<Post>
	 * find3PostFirst();
	 */

	@Query("SELECT p FROM Post p ORDER BY createDate DESC")
	List<Post> find3PostFirst();

	@Query(value = "SELECT * FROM Posts p JOIN motels m ON p.motel_id = m.motel_id JOIN accounts a ON m.account_id = a.account_id WHERE m.status = 1 and p.status = 1 and a.active = 1 and m.province LIKE %?1% ORDER BY ABS(DATEDIFF(DAY, p.create_date, GETDATE())) ASC", nativeQuery = true)
	List<Post> findPosts(String province);

	@Query(value = "SELECT * FROM Posts p JOIN motels m ON p.motel_id = m.motel_id JOIN accounts a ON a.account_id = m.account_id WHERE m.status = 1 and p.status = 1 and a.active= 1  ORDER BY ABS(DATEDIFF(DAY, p.create_date, GETDATE())) ASC", nativeQuery = true)
	List<Post> findPostsnew();

	@Query(value = "select * from posts a inner join motels b on a.motel_id = b.motel_id where a.motel_id IN (:listMotelId)", nativeQuery = true)
	List<Post> findPostsByMotel(@Param("listMotelId") List<Integer> listMotelId);

	@Query(value = "SELECT p FROM Post p WHERE p.motel.motelId = :motelId")
	List<Post> findPostsByMotelId(@Param("motelId") Integer motelId);

}
