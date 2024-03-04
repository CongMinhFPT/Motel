package com.motel.service;

import java.util.List;

import com.motel.entity.Tag;
import com.motel.exception.TagNotFoundException;
import com.motel.model.CreateTag;

public interface TagService {
    List<Tag> getListTag();

//    Tag createTag(CreateTag request);
//
//    Tag updateTag(int tagId, CreateTag request);

   // void enableTag(int tagId);

    void deleteTag(Integer tagId) throws TagNotFoundException;

	Tag save(Tag tag);

	Tag getId(Integer tagId);

}