package com.motel.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.motel.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>  {
	 List<Post> findByMotelRoom_Motel_DistrictAndMotelRoom_Motel_Province(String district, String province);
}