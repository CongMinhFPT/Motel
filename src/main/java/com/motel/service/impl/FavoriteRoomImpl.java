package com.motel.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.FavoriteRoom;
import com.motel.entity.MotelRoom;
import com.motel.repository.AccountsRepository;
import com.motel.repository.FavoriteRoomRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.FavoriteRoomService;

@Service
public class FavoriteRoomImpl implements FavoriteRoomService {

    @Autowired
    FavoriteRoomRepository favoriteRoomRepository;
    @Autowired
    AccountsRepository accountsRepository;
    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Override
    public FavoriteRoom addRoom(Integer accountId, Integer motelRoomId) {
        Account account = accountsRepository.findById(accountId).orElse(null);
        MotelRoom motelRoom = motelRoomRepository.findById(motelRoomId).orElse(null);

        FavoriteRoom favoriteRoom = new FavoriteRoom();

        favoriteRoom.setAccount(account);
        favoriteRoom.setCreateDate(new Date());
        favoriteRoom.setMotelRoom(motelRoom);
        favoriteRoom.setStatus(true);
        return favoriteRoomRepository.save(favoriteRoom);
    }

    @Override
    public void deleteRoom(Integer favoriteRoomId) {
        favoriteRoomRepository.deleteById(favoriteRoomId);
    }

    @Override
    public Page<MotelRoom> getPagedFavoriteRooms(Pageable pageable) {
        return motelRoomRepository.findMotelRoomByFavoriteRoom(pageable);
    }

}
