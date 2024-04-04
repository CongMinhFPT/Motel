package com.motel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.CategoryRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;

public interface MotelRoomService {
    List<MotelRoom> getAll();

    Optional<MotelRoom> getById(Integer motelRoomId);
    public Boolean CheckMotelRoomInMotel(int idmotel , int idmotelroom);
    public String GetAddMotelRoom ( Model model);
    public String PostAddMotelRoom (MotelRoom motelRoom,Model model,MultipartFile[] files , BindingResult bindingResult,RedirectAttributes attributes);
    public Boolean CheckImgAddMotelRoom(MultipartFile[] files);
    void ListCategoryRoom(Model model);
    public String ManageMotelRoom(Model model);
    public String GetUpdateMotelRoom(Model model , int idmotelroom);
    public String PostUpdateMotelRoom (MotelRoom motelRoom,Model model,MultipartFile[] files , BindingResult bindingResult,RedirectAttributes attributes);
    public Boolean CheckRoomInMotel(int idroom  , int idmotel);
    MotelRoom getMotelRoomId(Integer id);

}

