package com.motel.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.CategoryRoom;
import com.motel.repository.CategoryRoomRepository;
import com.motel.service.CategoryService;

@Service
public class CategoryImpl implements CategoryService {

	@Autowired
	CategoryRoomRepository categoryRepo;

	@Override
	public List<CategoryRoom> findActive() {

		return categoryRepo.findActive();
	}

	@Override
	public CategoryRoom findById(Integer id) {
		return categoryRepo.findById(id).get();
	}

	@Override
	public CategoryRoom create(CategoryRoom category) {
		return categoryRepo.save(category);
	}

	@Override
	public void changeStatus(Integer id, boolean newStatus) {
		Optional<CategoryRoom> categoryOptional = categoryRepo.findById(id);
		categoryOptional.ifPresent(category -> {
			category.setStatus(newStatus);
			categoryRepo.save(category);
		});
	}
}
