package com.motel.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.repository.RenterRepository;
import com.motel.service.RenterService;

@Controller
public class RenterController {

    @Autowired
    RenterService renterService;

    @Autowired
    RenterRepository renterRepository;

    @GetMapping("/admin/renter/add-renter")
    public String getAddRenter(Model model) {
        // List<MotelRoom> motelRooms = renterService.getAll();
        // model.addAttribute("motelRooms", motelRooms);
        return "/admin/renter/add-renter";
    }

    @GetMapping("/admin/renter")
    public String getListRenter(Model model) {
        List<Renter> renters = renterService.getRenters();
        model.addAttribute("renters", renters);
        return "/admin/renter/renter-list";
    }

    @GetMapping("/admin/renter/update-renter/{renterId}")
    public String getRenter(@PathVariable("renterId") Integer renterId, Model model) {
        Renter renter = renterService.getRenter(renterId);
        model.addAttribute("renter", renter);
        List<MotelRoom> motelRooms = renterService.getAll();
        model.addAttribute("motelRooms", motelRooms);
        return "/admin/renter/update-renter";
    }

    @PostMapping("/admin/renter/update-renter/{renterId}")
    public String getUpdate(@PathVariable("renterId") Integer renterId, Model model,
            @ModelAttribute("renter") Renter renter) {
        List<MotelRoom> motelRooms = renterService.getAll();
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
