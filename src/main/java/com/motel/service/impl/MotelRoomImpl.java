package com.motel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.MotelRoom;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.MotelRoomService;

@Service
public class MotelRoomImpl implements MotelRoomService{

    @Autowired MotelRoomRepository motelRoomRepository;

    @Override
    public List<MotelRoom> getAll() {
        return motelRoomRepository.findAll();
    }
    
}
