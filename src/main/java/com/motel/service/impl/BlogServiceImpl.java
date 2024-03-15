package com.motel.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.motel.Until.page.PagingAndSortingHelper;
import com.motel.entity.Account;
import com.motel.entity.Blog;
import com.motel.exception.BlogNotFoundException;
import com.motel.repository.AccountsRepository;
import com.motel.repository.BlogRepository;
import com.motel.repository.TagRepository;
import com.motel.service.BlogService;

@Transactional
@Service
public class BlogServiceImpl implements BlogService {

	@SuppressWarnings("unused")
	@Autowired
	private TagRepository tagRepo;
	@Autowired
	private BlogRepository blogRepo;
	@Autowired
	private AccountsRepository acRepo;
	public static final int BLOG_PER_PAGE = 5;

	@Override
	public List<Blog> getListBlog() {
		// TODO Auto-generated method stub
		return (List<Blog>) blogRepo.findAll();
	}

	@Override
	public void deleteBlog(Integer blogId) throws BlogNotFoundException {
		Blog blog = blogRepo.findById(blogId).get();

		if (blog == null) {
			throw new BlogNotFoundException("không tìm thấy blog");
		}
		blogRepo.deleteByBlogId(blogId);
	}

	@Override
	public Blog save(Blog blog) {
		if (blog.getBlogId() == null) {
			blog.setCreateDate(new Date());
		} else {
			Blog blogInDB = blogRepo.findById(blog.getBlogId()).get();
			blog.setCreateDate(blogInDB.getCreateDate());
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Account account = acRepo.getByEmail(email);

		blog.setAccount(account);

		return blogRepo.save(blog);
	}

	@SuppressWarnings("null")
	@Override
	public Blog getId(Integer blogId) {
		return blogRepo.findById(blogId).get();
	}


	@Override
	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer tagId) {
		Pageable pageable = helper.createPageable(BLOG_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		Page<Blog> page = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			if(tagId != null && tagId > 0) {
				page = blogRepo.searchInTag(tagId, keyword, pageable);
			} else {
				page = blogRepo.findAll(keyword, pageable);
			}
		}else {
			if(tagId != null && tagId > 0) {
				page = blogRepo.findAllInTag(tagId, pageable);
			}else {
				page = blogRepo.findAll(pageable);
			}
		}
		helper.updateModelAttributes(pageNum, page);

	}

	@Override
	public Page<Blog> listByPage(int pageNum, Integer tagId) {
		int pageSize = BLOG_PER_PAGE;

	    if (tagId != null && tagId > 0) {
	        return blogRepo.findAllInBlog(tagId, PageRequest.of(pageNum - 1, pageSize));
	    } else {
	        return blogRepo.findAll(PageRequest.of(pageNum - 1, pageSize));
	    }
	}

	@Override
	public List<Blog> findBlogSimilar(Integer tagId) {
		// TODO Auto-generated method stub
		return blogRepo.listSimilar(tagId);
	}


}
