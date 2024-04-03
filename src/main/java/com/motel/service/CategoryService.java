package com.motel.service;

import java.util.List;

import com.motel.entity.CategoryRoom;

public interface CategoryService {

	List<CategoryRoom> findActive();

	CategoryRoom findById(Integer id);

	CategoryRoom create(CategoryRoom category);

	void changeStatus(Integer id, boolean newStatus);

}
