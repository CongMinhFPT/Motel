package com.motel.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.CategoryRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
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
    private CategoryRoomRepository categoryRepository;
    
    public Page<roomDTO> getAllRoomDTOs(Pageable pageable) {
        Page<Motel> motelsPage = motelRepository.findAll(pageable);
        return motelsPage.map(this::convertToDTO);
    }


    private roomDTO convertToDTO(Motel motel) {
        roomDTO dto = new roomDTO();
        dto.setImage(motel.getImage());
        dto.setAddress(motel.getDetailAddress() + ", " + motel.getWard());
        // You need to retrieve a MotelRoom object associated with the Motel here
        MotelRoom room = motel.getMotelRoom().get(0); // Assuming there is only one room associated with a motel
        dto.setPrice(room.getPrice());
        dto.setArea(room.getLength() * room.getWidth());
        if (room.getCategoryRoom() != null) {
            dto.setTitle(room.getCategoryRoom().getTitle());
        }
        if (motel.getAccount() != null) {
            dto.setFullname(motel.getAccount().getFullname());
        }
        dto.setCreateDate(motel.getCreateDate());
        dto.setDistrict(motel.getDistrict());
        dto.setCity(motel.getProvince());
        return dto;
    }

    public List<CategoryRoom> findAllCategory() {
        return categoryRepository.findAll();
    }
}
