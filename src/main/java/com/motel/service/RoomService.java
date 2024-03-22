package com.motel.service;

import java.util.List;

import com.motel.entity.Account;
import com.motel.entity.CategoryRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;

public interface RoomService {

	List<MotelRoom> findAllMotelRoom();

	List<Motel> findAllMotel();

	List<Account> findAllAccount();

	List<CategoryRoom> findAllCategory();


}
