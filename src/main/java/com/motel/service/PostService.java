package com.motel.service;

import java.util.List;

import com.motel.entity.Post;
import com.motel.exception.PostNotFoundException;

public interface PostService {
   
    List<Post> getListBlog();
    
    void deleteBlog(Integer blogId) throws PostNotFoundException;

    Post save(Post blog);

   	Post getId(Integer blogId);
}
