package com.motel.service;

import java.util.List;

import com.motel.entity.Post;

public interface PostService {

	
	List<Post> getListPost();
	
	Post save(Post post, String email);

	Post update(Post post, Integer postId);
	
	Post getId(Integer postId);

	List<Post> getListPostEnable();

	void deletePost(Integer postId);
	
}
