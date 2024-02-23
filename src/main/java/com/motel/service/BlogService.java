package com.motel.service;

import java.util.List;

import com.motel.entity.Blog;
import com.motel.model.CreateBlog;

import javassist.NotFoundException;


public interface BlogService {
    List<Blog> getList();

    List<Blog> getListNewest(Integer limit);

    Blog getBlog(int blogId) throws NotFoundException;

    Blog createBlog(CreateBlog request) throws NotFoundException;

    Blog updateBlog(int blogId, CreateBlog request) throws NotFoundException;

    void deleteBlog(int blogId);

}