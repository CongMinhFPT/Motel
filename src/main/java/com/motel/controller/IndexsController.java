package com.motel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.motel.entity.Account;
import com.motel.entity.CustomUserDetails;
import com.motel.entity.Indexs;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.model.IndexsModel;
import com.motel.repository.AccountsRepository;
import com.motel.repository.IndexsRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.IndexsService;
import com.motel.service.RenterService;
import com.motel.service.impl.ManageMotelImpl;

@Controller
public class IndexsController {

    @Autowired
    IndexsRepository indexsRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Autowired
    IndexsService indexsService;

    @Autowired
    RenterService renterService;

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    ManageMotelImpl manageMotelImpl;

    @Autowired
    MotelRepository motelRepository;

    @GetMapping("/admin/indexs")
    public String getFormIndexs(Model model) {
        if (manageMotelImpl.CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = manageMotelImpl.CheckLogin().get();
            if (manageMotelImpl.CheckAccountSetIdMotel(customUserDetails)) {
                Motel motel = motelRepository.getById(customUserDetails.getMotelid());

                List<MotelRoom> motelRooms = motel.getMotelRoom();
                List<Indexs> indexs = new ArrayList<>();

                for (MotelRoom motelRoom : motelRooms) {
                    for (Indexs indexes : motelRoom.getIndex()) {
                        indexs.add(indexes);
                    }
                }

                indexs.sort(Comparator.comparing(Indexs::getCreateDate).reversed());

                model.addAttribute("indexs", indexs);
                manageMotelImpl.SetModelMotel(model);
                return "/admin/indexs/indexs-list";
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }

    }

    @GetMapping("/admin/indexs/add-indexs")
    public String getFormAddIndexs(@ModelAttribute("indexs") IndexsModel indexsModel, Model model,
            Authentication authentication) {

        String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);
        // model.addAttribute("accountId", account.getAccountId());

        List<MotelRoom> motelRooms = renterService.getMotelRoomByAccount(account.getAccountId());
        model.addAttribute("motelRooms", motelRooms);

        return "/admin/indexs/add-indexs";
    }

    @PostMapping("/admin/indexs/add-indexs")
    public String addIndexs(@ModelAttribute("indexs") IndexsModel indexsModel, Model model,
            Authentication authentication) {
        String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);
        // model.addAttribute("accountId", account.getAccountId());

        List<MotelRoom> motelRooms = renterService.getMotelRoomByAccount(account.getAccountId());
        model.addAttribute("motelRooms", motelRooms);

        try {
            indexsService.addIndexs(indexsModel);
            model.addAttribute("success", true);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/admin/indexs/add-indexs";
        }

        return "redirect:/admin/indexs";
    }

    @GetMapping("/admin/indexs/update-indexs/{indexsId}")
    public String getFormUpdateIndexs(@PathVariable("indexsId") Integer indexsId,
            @ModelAttribute("indexs") Indexs indexs, Model model) {
        Indexs indexsById = indexsRepository.getById(indexsId);
        model.addAttribute("indexes", indexsById);
        return "/admin/indexs/update-indexs";
    }

    @PostMapping("/admin/indexs/update-indexs/{indexsId}")
    public String updateIndexs(@PathVariable("indexsId") Integer indexsId, @ModelAttribute("indexs") Indexs indexs,
            Model model) {
        Indexs indexsById = indexsRepository.getById(indexsId);
        model.addAttribute("indexes", indexsById);

        try {
            indexsService.updateIndexs(indexsId, indexs);
            model.addAttribute("success", true);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "/admin/indexs/update-indexs";
        }

        return "redirect:/admin/indexs";
    }

    @GetMapping("/admin/indexs/delete-indexs/{motelRoomId}")
    public String deleteRenter(@PathVariable("motelRoomId") Integer motelRoomId, Model model) {
        indexsService.deleteIndexs(motelRoomId);
        return "redirect:/admin/indexs";
    }
}
