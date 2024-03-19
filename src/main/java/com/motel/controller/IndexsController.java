package com.motel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.motel.entity.Indexs;
import com.motel.entity.MotelRoom;
import com.motel.model.IndexsModel;
import com.motel.repository.IndexsRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.service.IndexsService;

@Controller
public class IndexsController {

    @Autowired
    IndexsRepository indexsRepository;

    @Autowired
    MotelRoomRepository motelRoomRepository;

    @Autowired
    IndexsService indexsService;

    @GetMapping("/admin/indexs")
    public String getFormIndexs(Model model) {
        List<Indexs> indexs = indexsRepository.findAllDESC();
        model.addAttribute("indexs", indexs);
        return "/admin/indexs/indexs-list";
    }

    @GetMapping("/admin/indexs/add-indexs")
    public String getFormAddIndexs(@ModelAttribute("indexs") IndexsModel indexsModel, Model model) {
        List<MotelRoom> motelRooms = motelRoomRepository.findAll();
        model.addAttribute("motelRooms", motelRooms);

        return "/admin/indexs/add-indexs";
    }

    @PostMapping("/admin/indexs/add-indexs")
    public String addIndexs(@ModelAttribute("indexs") IndexsModel indexsModel, Model model) {
        List<MotelRoom> motelRooms = motelRoomRepository.findAll();
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
