package com.motel.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.CategoryRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.RoomCash;
import com.motel.repository.AccountsRepository;
import com.motel.repository.CategoryRoomRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;

import DTO.roomDTO;


@Service
public class RoomService {
    @Autowired
    private MotelRepository motelRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;
    
    @Autowired
    private CategoryRoomRepository categoryRepository;
    
    public Page<MotelRoom> getAllRoomDTOs(Pageable pageable) {
       
        Pageable pageableWithSize5 = PageRequest.of(pageable.getPageNumber(), 10);
        
        
        Page<MotelRoom> motelsPage = motelRoomRepository.findAll(pageableWithSize5);
       
        return motelsPage;
    }


    private roomDTO convertToDTO(Motel motel) {
        roomDTO dto = new roomDTO();
        dto.setImage(motel.getImage());
        dto.setAddress(motel.getDetailAddress());
        
        MotelRoom room = motel.getMotelRoom().isEmpty() ? null : motel.getMotelRoom().get(0);
        dto.setArea(room.getLength() * room.getWidth());
        if (room.getCategoryRoom() != null) {
            dto.setTitle(room.getCategoryRoom().getTitle());
        }
        if (motel.getAccount() != null) {
            dto.setFullname(motel.getAccount().getFullname());
        }
        dto.setCreateDate(motel.getCreateDate());
        dto.setCity(motel.getProvince());
        dto.setDistrict(motel.getDistrict());
        dto.setWard(motel.getWard());
        
        // Lấy roomCash từ danh sách roomCash của room
        List<RoomCash> roomCashList = room.getRoomCash();
        if (!roomCashList.isEmpty()) {
            // Lấy roomBill từ phần tử đầu tiên trong danh sách roomCash
            dto.setPrice(roomCashList.get(0).getRoomBill());
        }
        
        return dto;
    }

    public List<CategoryRoom> findAllCategory() {
        return categoryRepository.findAll();
    }
}
