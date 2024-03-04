package com.motel.service;

import java.util.List;

import com.motel.Until.PagingAndSortingHelper;
import com.motel.entity.Blog;
import com.motel.exception.BlogNotFoundException;


public interface BlogService {

     List<Blog> getListBlog();

    List<Blog> getListNewest(int limit);

//    Blog getBlog(int blogId);
//
//    Blog createBlog(CreateBlog request);
//
//    Blog updateBlog(int blogId,CreateBlog request);
//
//    void deleteBlog(int blogId);
    
    void deleteBlog(Integer blogId) throws BlogNotFoundException;

   	Blog save(Blog blog);

   	Blog getId(Integer blogId);

	void listByPage(int pageNum, PagingAndSortingHelper helper);

}
