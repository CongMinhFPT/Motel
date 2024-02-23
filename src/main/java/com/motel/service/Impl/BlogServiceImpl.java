package com.motel.service.Impl;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.motel.entity.Blog;
import com.motel.entity.Image;
import com.motel.entity.Tag;
import com.motel.model.CreateBlog;
import com.motel.repository.BlogRepository;
import com.motel.repository.ImageRepository;
import com.motel.repository.TagRepository;
import com.motel.service.BlogService;

import javassist.NotFoundException;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private TagRepository tagRepository;

    @SuppressWarnings("unused")
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Blog> getList() {
        return blogRepository.findAll(Sort.by("id").descending());
    }

    @SuppressWarnings("")
    @Override
    public Blog createBlog(CreateBlog request) throws NotFoundException {
        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setDescriptions(request.getDescriptions());

        @SuppressWarnings({ "null", "unused" })
        Image image = imageRepository.findById(request.getImageId())
                .orElseThrow(() -> new NotFoundException("Not Found Image"));
        // blog.setImage(image);

        blog.setCreateDate(new Timestamp(System.currentTimeMillis()));
        Set<Tag> tags = new HashSet<>();
        for (Integer tagId : request.getTags()) {
            @SuppressWarnings("null")
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException("Not Found Tag"));
            tags.add(tag);

        }
        // blog.setTag(tags);
        blogRepository.save(blog);
        return blog;

    }

    @Override
    public List<Blog> getListNewest(Integer limit) {
        List<Blog> list = blogRepository.getListNewest(0);
        return list;
    }

    @Override
    public Blog getBlog(int blogId) throws NotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Not Found Blog"));
        return blog;
    }

    @SuppressWarnings("unused")
    @Override
    public Blog updateBlog(int blogId, CreateBlog request) throws NotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Not Found Blog"));
        blog.setTitle(request.getTitle());
        blog.setDescriptions(request.getDescriptions());
        @SuppressWarnings("null")
        Image image = imageRepository.findById(request.getImageId())
                .orElseThrow(() -> new NotFoundException("Not Found Image"));
        // blog.setImage(image);

        blog.setCreateDate(new Timestamp(System.currentTimeMillis()));
        Set<Tag> tags = new HashSet<>();
        for (Integer tagId : request.getTags()) {
            @SuppressWarnings("null")
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException("Not Found Tag"));
            tags.add(tag);

        }
        // blog.setTag(tags);
        blogRepository.save(blog);

        return blog;
    }

    @Override
    public void deleteBlog(int blogId) {
       
    }

   

}
