package com.motel.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Tag;
import com.motel.exception.TagNotFoundException;
import com.motel.model.CreateTag;
import com.motel.repository.TagRepository;
import com.motel.service.TagService;

@Transactional
@Service
public class TagServiceImpl implements TagService{

	@Autowired
	private TagRepository tagRepo;
	
	@Override
	public List<Tag> getListTag() {
		// TODO Auto-generated method stub
		return (List<Tag>) tagRepo.findAll();
	}
	@Override
	public void deleteTag(Integer tagId) throws TagNotFoundException {
		// TODO Auto-generated method stub
		Tag tag = tagRepo.findById(tagId).get();
		
		if(tag == null) {
			throw new TagNotFoundException("không tìm thấy tag");
		}
		tagRepo.deleteById(tagId);
	}

	@Override
	public Tag save(Tag tag) {
		// TODO Auto-generated method stub
		return tagRepo.save(tag);
		
	}

	@Override
	public Tag getId(Integer tagId) {
		// TODO Auto-generated method stub
		return tagRepo.findById(tagId).get();
	}

}
