package com.motel.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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
import com.motel.entity.RoomStatus;
import com.motel.repository.CategoryRoomRepository;
import com.motel.repository.ImageRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RoomStatusRepository;
import com.motel.service.MotelRoomService;

@Service
@Transactional
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
    ImageRepository imageRepository;
    @Autowired 
    RoomStatusRepository roomStatusRepository;

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
                    if (files == null || files.length <= 0 || files[0].isEmpty()) {
                        model.addAttribute("Imgerror", "Vui lòng chọn ảnh");
                    }
                    return "/admin/motel/add-room";
                } else {
                    if (motelRoom.getCategoryRoom().getCategoryRoomId() == null) {
                        ListCategoryRoom(model);
                        model.addAttribute("categoryRoomerror", "Vui lòng chọn loại phòng");
                        if (files == null || files.length <= 0 || files[0].isEmpty()) {
                            model.addAttribute("Imgerror", "Vui lòng chọn ảnh");
                        }
                        return "/admin/motel/add-room";
                    } else {
                        if (files != null && files.length > 0 && !files[0].isEmpty()) {
                            if (motelRoom.getVideo() != null) {
                                String[] parts = motelRoom.getVideo().split("be/");
                                String chuoi = parts[parts.length - 1];
                                motelRoom.setVideo(chuoi);
                            }
                            RoomStatus roomStatus = roomStatusRepository.getById(1);
                            motelRoom.setRoomStatus(roomStatus);
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
                                attributes.addFlashAttribute("successMessageAddRoom", "Thêm phòng trọ thành công");
                                return "redirect:/admin/manage-motelroom";
                            } else {
                                MultipartFile[] first6Files = Arrays.copyOfRange(files, 0, Math.min(files.length, 6));
                                List<String> listname = fileManager.save("ImgMotelRoom", first6Files);
                                listname.forEach(a -> {
                                    Image image = new Image();
                                    image.setMotelRoom(idmotelroom);
                                    image.setName(a);
                                    imageRepository.save(image);
                                });
                                attributes.addFlashAttribute("successMessageAddRoom", "Thêm phòng trọ thành công");
                                return "redirect:/admin/manage-motelroom";
                            }
                        } else {
                            model.addAttribute("Imgerror", "Vui lòng chọn ảnh");
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

    @Override
    public String GetUpdateMotelRoom(Model model, int idmotelroom) {
        if (impl.CheckLogin().isPresent()) {
            CustomUserDetails condition = impl.CheckLogin().get();
            if (impl.CheckAccountSetIdMotel(condition)) {
                if (CheckRoomInMotel(idmotelroom, condition.getMotelid())) {
                    impl.SetModelMotel(model);
                    MotelRoom motelRoom = motelRoomRepository.getById(idmotelroom);
                    model.addAttribute("motelroom", motelRoom);
                    ListCategoryRoom(model);
                    List<String> nameimg = new ArrayList<>();
                    if (!motelRoom.getImage().isEmpty() || motelRoom.getImage() != null) {
                        motelRoom.getImage().forEach(a -> {
                            nameimg.add(a.getName());
                        });
                        model.addAttribute("listimg", nameimg);
                    }
                    return "/admin/motel/update-room";
                } else {
                    return "/admin/error/error404";
                }
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

    @Override
    public String PostUpdateMotelRoom(MotelRoom motelRoom, Model model, MultipartFile[] files,
            BindingResult bindingResult, RedirectAttributes attributes) {
        if (impl.CheckLogin().isPresent()) {
            CustomUserDetails condition = impl.CheckLogin().get();
            if (impl.CheckAccountSetIdMotel(condition)) {
                if (CheckRoomInMotel(motelRoom.getMotelRoomId(), condition.getMotelid())) {
                    MotelRoom motelRoom2 =motelRoomRepository.getById(motelRoom.getMotelRoomId());
                    impl.SetModelMotel(model);
                    if (bindingResult.hasErrors()) {
                        ListCategoryRoom(model);
                        if (motelRoom.getCategoryRoom().getCategoryRoomId() == null) {
                            model.addAttribute("categoryRoomerror", "Vui lòng chọn loại phòng");
                        }
                        return "/admin/motel/update-room";
                    } else {
                        if (motelRoom.getCategoryRoom().getCategoryRoomId() == null) {
                            ListCategoryRoom(model);
                            model.addAttribute("categoryRoomerror", "Vui lòng chọn loại phòng");
                            return "/admin/motel/update-room";
                        } else {
                            if (files != null && files.length > 0 && !files[0].isEmpty()) {
                                motelRoom2.setDescriptions(motelRoom.getDescriptions());
                                motelRoom2.setLength(motelRoom.getLength());
                                motelRoom2.setWidth(motelRoom.getWidth());
                                if (motelRoom.getVideo() != null) {
                                    String[] parts = motelRoom.getVideo().split("be/");
                                    String chuoi = parts[parts.length - 1];
                                    motelRoom2.setVideo(chuoi);
                                }
                              if (motelRoom2.getCategoryRoom().getCategoryRoomId()!=motelRoom.getCategoryRoom().getCategoryRoomId()) {
                                CategoryRoom categoryRoom = categoryRoomRepository
                                .getById(motelRoom.getCategoryRoom().getCategoryRoomId());
                                motelRoom2.setCategoryRoom(categoryRoom);
                              }
                                  motelRoomRepository.save(motelRoom2);
                                if (CheckImgAddMotelRoom(files)) {
                                    MotelRoom motelRoom3 =motelRoomRepository.getById(motelRoom.getMotelRoomId());
                                    motelRoom3.getImage().forEach(a -> {
                                        fileManager.delete("ImgMotelRoom", a.getName());
                                    });
                                    List<Image> images = motelRoom3.getImage();
                                    for (Image image : images) {
                                      image.setMotelRoom(null);
                                      imageRepository.save(image);
                                    }
                                    imageRepository.deleteAllByMotelRoomIsNull();
                                    List<String> listname = fileManager.save("ImgMotelRoom", files);
                                     listname.forEach(a -> {
                                        Image image = new Image();
                                        image.setMotelRoom(motelRoom3);
                                        image.setName(a);
                                        imageRepository.save(image);
                                    });
                                    attributes.addFlashAttribute("successMessageAddRoom", "Cập nhật phòng trọ thành công");
                                    return "redirect:/admin/update-motelroom/"+motelRoom.getMotelRoomId();
                                } else {
                                    MotelRoom motelRoom3 = motelRoomRepository.getById(motelRoom.getMotelRoomId());
                                    motelRoom3.getImage().forEach(a -> {
                                        fileManager.delete("ImgMotelRoom", a.getName());
                                    });
                                    for (Image image : motelRoom3.getImage()) {
                                        image.setMotelRoom(null);
                                        imageRepository.save(image);
                                    }
                                    imageRepository.deleteAllByMotelRoomIsNull();
                                    MultipartFile[] first6Files = Arrays.copyOfRange(files, 0,
                                            Math.min(files.length, 6));
                                    List<String> listname = fileManager.save("ImgMotelRoom", first6Files);
                                    listname.forEach(a -> {
                                        Image image = new Image();
                                        image.setMotelRoom(motelRoom3);
                                        image.setName(a);
                                        imageRepository.save(image);
                                    });
                                    attributes.addFlashAttribute("successMessageAddRoom", "Cập nhật phòng trọ thành công");
                                    return "redirect:/admin/update-motelroom/"+motelRoom.getMotelRoomId();
                                }
                            } else {
                                motelRoom2.setDescriptions(motelRoom.getDescriptions());
                                motelRoom2.setLength(motelRoom.getLength());
                                motelRoom2.setWidth(motelRoom.getWidth());
                                if (motelRoom.getVideo() != null) {
                                    String[] parts = motelRoom.getVideo().split("be/");
                                    String chuoi = parts[parts.length - 1];
                                    motelRoom2.setVideo(chuoi);
                                }
                              if (motelRoom2.getCategoryRoom().getCategoryRoomId()!=motelRoom.getCategoryRoom().getCategoryRoomId()) {
                                CategoryRoom categoryRoom = categoryRoomRepository
                                .getById(motelRoom.getCategoryRoom().getCategoryRoomId());
                                motelRoom2.setCategoryRoom(categoryRoom);
                              }
                                motelRoomRepository.save(motelRoom2);
                                attributes.addFlashAttribute("successMessageAddRoom", "Cập nhật phòng trọ thành công");
                                return "redirect:/admin/update-motelroom/"+motelRoom.getMotelRoomId();
                            }
                        }
                    }
                } else {
                    return "/admin/error/error404";
                }
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

    @Override
    public Boolean CheckRoomInMotel(int idroom, int motel) {
        Motel motel2 = motelRepository.getById(motel);
        for (MotelRoom motelRoom : motel2.getMotelRoom()) {
            if (motelRoom.getMotelRoomId() == idroom) {
                return true;
            }
        }
        return false;
    }

	@Override
	public MotelRoom getMotelRoomId(Integer id) {
		// TODO Auto-generated method stub
		return motelRoomRepository.findById(id).get();
	}


}
