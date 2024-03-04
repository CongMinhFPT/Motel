package com.motel.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.motel.Until.PagingAndSortingHelper;
import com.motel.entity.Account;
import com.motel.entity.Blog;
import com.motel.exception.BlogNotFoundException;
import com.motel.repository.AccountsRepository;
import com.motel.repository.BlogRepository;
import com.motel.repository.TagRepository;
import com.motel.service.BlogService;

@Transactional
@Service
public class BlogServiceImpl implements BlogService{

	@Autowired
	private TagRepository tagRepo;
	@Autowired
	private BlogRepository blogRepo;
	@Autowired
	private AccountsRepository acRepo;
	
	@Override
	public List<Blog> getListBlog() {
		// TODO Auto-generated method stub
		return (List<Blog>) blogRepo.findAll();
	}
	@Override
	public void deleteBlog(Integer blogId) throws BlogNotFoundException {
		// TODO Auto-generated method stub
		Blog blog = blogRepo.findById(blogId).get();
		
		if(blog == null) {
			throw new BlogNotFoundException("không tìm thấy blog");
		}
		blogRepo.deleteById(blogId);
	}
	@Override
	public Blog save(Blog blog) {
		// TODO Auto-generated method stub
		if (blog.getBlogId() == null) {
			blog.setCreateDate(new Date());
		} else {
			Blog blogInDB = blogRepo.findById(blog.getBlogId()).get();
			blogInDB.setCreateDate(blogInDB.getCreateDate());
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Account account = acRepo.getByEmail(email);
		
		blog.setAccount(account);
		
		return blogRepo.save(blog);
	}
	@Override
	public Blog getId(Integer blogId) {
		// TODO Auto-generated method stub
		return blogRepo.findById(blogId).get();
	}
	@Override
	public List<Blog> getListNewest(int limit) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		// TODO Auto-generated method stub
		
	}

	
}
