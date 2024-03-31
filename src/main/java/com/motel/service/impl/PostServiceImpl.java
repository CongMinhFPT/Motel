package com.motel.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.Post;
import com.motel.repository.AccountsRepository;
import com.motel.repository.PostRepository;
import com.motel.service.PostService;

@Transactional
@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepo;
	@Autowired
	private AccountsRepository acRepo;
	
	@Override
	public List<Post> getListPost() {
		// TODO Auto-generated method stub
		return (List<Post>) postRepo.findAll();
	}

	@Override
	public Post save(Post post, String email) {
		// TODO Auto-generated method stub
		if (post.getPostId() == null) {
			post.setCreateDate(new Date());
			post.setStatus(false);
		} else {
			Post postInDB = postRepo.findById(post.getPostId()).get();
			post.setCreateDate(postInDB.getCreateDate());
		}
		
		Account account = acRepo.getByEmail(email);
		post.setAccount(account);
		
		return postRepo.save(post);
	}

	@Override
	public Post getId(Integer postId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> getListPostEnable() {
		// TODO Auto-generated method stub
		return postRepo.getListPostEnable();
	}

}

