package com.motel.service;

import java.util.List;

import com.motel.entity.Tag;
import com.motel.exception.TagNotFoundException;

public interface TagService {
    List<Tag> getListTag();

    void deleteTag(Integer tagId) throws TagNotFoundException;

	Tag save(Tag tag);

	Tag getId(Integer tagId);

}