package com.motel.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.motel.Until.page.PagingAndSortingHelper;
import com.motel.entity.Blog;
import com.motel.exception.BlogNotFoundException;


public interface BlogService {
	
	public static final int BLOG_PER_PAGE = 5;

     List<Blog> getListBlog();

    //List<Blog> getListNewest(int limit);

    
    void deleteBlog(Integer blogId) throws BlogNotFoundException;

   	Blog save(Blog blog);

   	Blog getId(Integer blogId);

	void listByPage(int pageNum,PagingAndSortingHelper helper, Integer tagId);

	Page<Blog> listByPage(int pageNum, Integer tagId);

	List<Blog> findBlogSimilar(Integer tagId);


}
