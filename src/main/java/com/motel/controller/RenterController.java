package com.motel.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.RenterRepository;
import com.motel.service.RenterService;
import com.motel.service.impl.ManageMotelImpl;

@Controller
public class RenterController {

    @Autowired
    RenterService renterService;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    ManageMotelImpl manageMotelImpl;

    @Autowired
    MotelRepository motelRepository;

    @GetMapping("/admin/renter/add-renter")
    public String getAddRenter(Model model, Authentication authentication) {
        String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);
        model.addAttribute("accountIdRenter", account.getAccountId());
        manageMotelImpl.CheckLoginAndSetMotelid(model);
        // List<MotelRoom> motelRooms = renterService.getAll();
        // model.addAttribute("motelRooms", motelRooms);
        return "/admin/renter/add-renter";
    }

    @GetMapping("/admin/renter")
    public String getListRenter(Model model) {
        if (manageMotelImpl.CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = manageMotelImpl.CheckLogin().get();
            if (manageMotelImpl.CheckAccountSetIdMotel(customUserDetails)) {
                Motel motel = motelRepository.getById(customUserDetails.getMotelid());

                List<MotelRoom> motelRooms = motel.getMotelRoom();
                List<Renter> renters = new ArrayList<>();

                for (MotelRoom motelRoom : motelRooms) {
                    for (Renter renter : motelRoom.getRenter()) {
                        renters.add(renter);
                    }
                }

                renters.sort(Comparator.comparing(Renter::getRenterDate).reversed());
                manageMotelImpl.SetModelMotel(model);
                model.addAttribute("renters", renters);
                return "/admin/renter/renter-list";
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }

    }

    @GetMapping("/admin/renter/update-renter/{renterId}")
    public String getRenter(@PathVariable("renterId") Integer renterId, Model model, Authentication authentication) {
        Renter renter = renterService.getRenter(renterId);
        model.addAttribute("renter", renter);

        String emailAccount = authentication.getName();
        Account account = accountsRepository.getByEmail(emailAccount);

        List<MotelRoom> motelRooms = renterService.getMotelRoomByAccount(account.getAccountId());

        System.out.println(motelRooms);
        model.addAttribute("motelRooms", motelRooms);
        return "/admin/renter/update-renter";
    }

    @PostMapping("/admin/renter/update-renter/{renterId}")
    public String getUpdate(@PathVariable("renterId") Integer renterId, Model model,
            @ModelAttribute("renter") Renter renter) {
        List<MotelRoom> motelRooms = renterService.getMotelRoomByAccount(renter.getAccount().getAccountId());
        model.addAttribute("motelRooms", motelRooms);
        Renter renterCurrent = renterService.getRenter(renterId);

        renterRepository.save(renter);
        model.addAttribute("successUpdate", true);
        return "/admin/renter/update-renter";
    }

    @GetMapping("/admin/deleteRenter/{renterId}")
    public String deleteRenter(@PathVariable("renterId") Integer renterId, Model model) {
        renterService.deleteRenter(renterId);
        return "redirect:/admin/renter";
    }
}
