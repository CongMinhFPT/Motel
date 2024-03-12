package com.motel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.CategoryRoom;
import com.motel.entity.MotelRoom;
import com.motel.repository.CategoryRoomRepository;
import com.motel.service.CategoryRoomService;

@Service
public class CategoryRoomImpl implements CategoryRoomService{

	
	@Autowired
	CategoryRoomRepository repo;
	
	
	@Override
    public List<CategoryRoom> getAll() {
		return (List<CategoryRoom>) repo.findAll();
    }
}
