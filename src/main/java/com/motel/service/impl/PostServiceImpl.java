package com.motel.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Post;
import com.motel.exception.PostNotFoundException;
import com.motel.repository.PostRepository;
import com.motel.service.PostService;
@Transactional
@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepo;
	
	@Override
	public List<Post> getListBlog() {
		// TODO Auto-generated method stub
		return (List<Post>) postRepo.findAll();
	}

	@Override
	public void deleteBlog(Integer blogId) throws PostNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Post save(Post blog) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Post getId(Integer blogId) {
		// TODO Auto-generated method stub
		return null;
	}


}
