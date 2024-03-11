package com.motel.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.FileManager.FileManager;
import com.motel.entity.CategoryRoom;
import com.motel.entity.CustomUserDetails;
import com.motel.entity.Image;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.repository.CategoryRoomRepository;
import com.motel.repository.ImageRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.MotelRoomService;

@Service
public class MotelRoomImpl implements MotelRoomService {

    @Autowired
    MotelRoomRepository motelRoomRepository;
    @Autowired
    MotelRepository motelRepository;
    @Autowired
    ManageMotelImpl impl;
    @Autowired
    CategoryRoomRepository categoryRoomRepository;

    @Autowired
    FileManager fileManager;
    @Autowired
    ImageRepository imageRepository;;

    @Override
    public List<MotelRoom> getAll() {
        return motelRoomRepository.findAll();
    }

    @Override
    public Optional<MotelRoom> getById(Integer motelRoomId) {
        return motelRoomRepository.findById(motelRoomId);
    }

    @Override
    public Boolean CheckMotelRoomInMotel(int idmotel, int idmotelroom) {
        Motel aMotel = motelRepository.getById(idmotel);
        List<MotelRoom> listmMotelRooms = aMotel.getMotelRoom();
        for (MotelRoom a : listmMotelRooms) {
            if (a.getMotelRoomId() == idmotelroom) {
                return true;

            }
        }
        return false;
    }

    @Override
    public String GetAddMotelRoom(Model model) {
        if (impl.CheckLogin().isPresent()) {
            CustomUserDetails condition = impl.CheckLogin().get();
            if (impl.CheckAccountSetIdMotel(condition)) {
                impl.SetModelMotel(model);
                ListCategoryRoom(model);
                return "/admin/motel/add-room";
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }

    }

    @Override
    public String PostAddMotelRoom(MotelRoom motelRoom, Model model, MultipartFile[] files, BindingResult bindingResult,
            RedirectAttributes attributes) {
        if (impl.CheckLogin().isPresent()) {
            CustomUserDetails condition = impl.CheckLogin().get();
            if (impl.CheckAccountSetIdMotel(condition)) {
                impl.SetModelMotel(model);
                if (bindingResult.hasErrors()) {
                    ListCategoryRoom(model);
                    System.out.println(motelRoom.getCategoryRoom());
                    if (motelRoom.getCategoryRoom().getCategoryRoomId() == null) {
                        model.addAttribute("categoryRoomerror", "Vui lòng chọn loại phòng");
                    }
                    return "/admin/motel/add-room";
                } else {
                    if (motelRoom.getCategoryRoom().getCategoryRoomId() == null) {
                        ListCategoryRoom(model);
                        model.addAttribute("categoryRoomerror", "Vui lòng chọn loại phòng");
                        return "/admin/motel/add-room";
                    } else {
                        if (files != null && files.length > 0 && !files[0].isEmpty()) {
                            if (motelRoom.getVideo() != null) {
                                String[] parts = motelRoom.getVideo().split("be/");
                                String chuoi = parts[parts.length - 1];
                                motelRoom.setVideo(chuoi);
                            }
                            CategoryRoom categoryRoom = categoryRoomRepository
                                    .getById(motelRoom.getCategoryRoom().getCategoryRoomId());
                            motelRoom.setCategoryRoom(categoryRoom);
                            Motel motel = motelRepository.getById(condition.getMotelid());
                            motelRoom.setMotel(motel);
                            MotelRoom idmotelroom = motelRoomRepository.save(motelRoom);
                            if (CheckImgAddMotelRoom(files)) {
                                List<String> listname = fileManager.save("ImgMotelRoom", files);
                                listname.forEach(a -> {
                                    Image image = new Image();
                                    image.setMotelRoom(idmotelroom);
                                    image.setName(a);
                                    imageRepository.save(image);
                                });
                                System.out.println("them thanh cong");
                                ListCategoryRoom(model);
                                return "/admin/motel/add-room";
                            } else {
                                MultipartFile[] first6Files = Arrays.copyOfRange(files, 0, Math.min(files.length, 6));
                                List<String> listname = fileManager.save("ImgMotelRoom", first6Files);
                                listname.forEach(a -> {
                                    Image image = new Image();
                                    image.setMotelRoom(idmotelroom);
                                    image.setName(a);
                                    imageRepository.save(image);
                                });
                                System.out.println("them thanh cong");
                                ListCategoryRoom(model);
                                return "/admin/motel/add-room";
                            }
                        } else {
                            if (motelRoom.getVideo() != null) {
                                String[] parts = motelRoom.getVideo().split("be/");
                                String chuoi = parts[parts.length - 1];
                                motelRoom.setVideo(chuoi);
                            }
                            CategoryRoom categoryRoom = categoryRoomRepository
                                    .getById(motelRoom.getCategoryRoom().getCategoryRoomId());
                            motelRoom.setCategoryRoom(categoryRoom);
                            Motel motel = motelRepository.getById(condition.getMotelid());
                            motelRoom.setMotel(motel);
                            motelRoomRepository.save(motelRoom);
                            System.out.println("them thanh cong");
                            ListCategoryRoom(model);
                            return "/admin/motel/add-room";
                        }
                    }
                }
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }

    }

    @Override
    public Boolean CheckImgAddMotelRoom(MultipartFile[] files) {
        if (files.length <= 6) {
            return true;
        }
        return false;
    }

    @Override
    public void ListCategoryRoom(Model model) {
        List<CategoryRoom> lisxCategoryRooms = categoryRoomRepository.findAll();
        model.addAttribute("categoryRoomlist", lisxCategoryRooms);
    }

    @Override
    public String ManageMotelRoom(Model model) {
        if (impl.CheckLogin().isPresent()) {
            CustomUserDetails condition = impl.CheckLogin().get();
            if (impl.CheckAccountSetIdMotel(condition)) {
                impl.SetModelMotel(model);
              List<MotelRoom> listMotelRooms = motelRepository.getById(condition.getMotelid()).getMotelRoom();
              model.addAttribute("ListMotelRooms", listMotelRooms);
                return "/admin/motel/manage-motelroom";
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

}
