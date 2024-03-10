package com.motel.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.motel.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>  {
}
