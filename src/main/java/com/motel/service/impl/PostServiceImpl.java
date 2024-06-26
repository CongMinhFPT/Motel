package com.motel.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.Post;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.PostRepository;
import com.motel.service.PostService;

@Transactional
@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepo;
	@Autowired
	private AccountsRepository acRepo;

	@Autowired
	MotelRoomRepository motelRoomRepository;

	@Autowired
	MotelRepository motelRepository;

	@Override
	public List<Post> getListPost() {
		// TODO Auto-generated method stub
		return (List<Post>) postRepo.findAll();
	}

	// @Override
	// public List<Post> getList3PostFirst() {
	// return (List<Post>) postRepo.find3PostFirst(PageRequest.of(0, 3));
	// /* return (List<Post>) postRepo.find3PostFirst(); */

	// }

	@Override
	public List<Post> getList3PostFirst() {
		return postRepo.find3PostFirst();
		/* return (List<Post>) postRepo.find3PostFirst(); */
	}

	@Override
	public Post save(Post post, String email) {
		// TODO Auto-generated method stub
		if (post.getPostId() == null) {
			post.setCreateDate(new Date());
			post.setStatus(true);
		} else {
			Post postInDB = postRepo.findById(post.getPostId()).get();
			post.setCreateDate(postInDB.getCreateDate());
		}

		Account account = acRepo.getByEmail(email);
		// post.setAccount(account);

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
		return postRepo.findAll();
	}

	@Override
	public Post update(Post post, Integer postId) {
		Post post2 = postRepo.findById(postId).orElse(null);

		post.setPostId(post2.getPostId());
		// post.setAccount(post2.getAccount());
		post.setCreateDate(post2.getCreateDate());
		Motel motel = motelRepository.findById(post2.getMotel().getMotelId()).orElse(null);

		post.setMotel(motel);
		post.setStatus(post2.isStatus());
		post.setTitle(post.getTitle());
		postRepo.save(post);
		return post;
	}

	@Override
	public void deletePost(Integer postId) {
		if (postId == null) {
			throw new IllegalStateException("Bài đăng không tồn tại!");
		} else {
			postRepo.deleteById(postId);
		}
	}

	@Override
	public List<Post> getListPostNonActive() {
		return postRepo.findPostByNonActive();
	}

	@Override
	public Post checkUpdate(Post post, Integer postId) {
		Post post2 = postRepo.findById(postId).orElse(null);

		post.setPostId(post2.getPostId());
		// post.setAccount(post2.getAccount());
		post.setCreateDate(post2.getCreateDate());

		// post.setMotelRoom(post2.getMotelRoom());
		post.setStatus(post.isStatus());
		post.setTitle(post2.getTitle());
		postRepo.save(post);
		return post;
	}

}
