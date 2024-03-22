package com.motel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.CategoryRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.repository.AccountsRepository;
import com.motel.repository.CategoryRoomRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.RoomService;

@Service
public class RoomImpl implements RoomService {

	@Autowired
	MotelRepository motelrep;

	@Autowired
	AccountsRepository accorep;

	@Autowired
	MotelRoomRepository motelRoomrep;

	@Autowired
	CategoryRoomRepository caterep;

	@Override
	public List<MotelRoom> findAllMotelRoom() {

		return motelRoomrep.findAll();
	}

	@Override
	public List<Motel> findAllMotel() {

		return motelrep.findAll();
	}

	@Override
	public List<Account> findAllAccount() {

		return accorep.findAll();
	}

	@Override
	public List<CategoryRoom> findAllCategory() {

		return caterep.findAll();
	}

	
}
