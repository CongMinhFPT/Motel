package com.motel.admin;

import java.util.ArrayList;

import javax.persistence.EntityNotFoundException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.CategoryRoom;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.WaterCash;
import com.motel.entity.WifiCash;
import com.motel.repository.CategoryRoomRepository;
import com.motel.repository.MotelRepository;
import com.motel.service.MotelRoomService;
import com.motel.service.impl.ManageMotelImpl;

@MultipartConfig
@Controller
public class AdminMotelController {
    @Autowired
    ManageMotelImpl motelImpl;
    @Autowired
    MotelRepository motelRepository;
    @Autowired
    CategoryRoomRepository categoryRoomRepository;
    @Autowired
    MotelRoomService roomService;

    @GetMapping("/admin/show-motel")
    public String showmotell( Model model) {
        return motelImpl.ShowMotel(model);
    }
    
    @GetMapping("/admin/add-motel")
    public String GetAddMotel(@ModelAttribute("motelttr") Motel motel ,Model model){
        return motelImpl.GetMotel(model);
    }
    @PostMapping("/admin/add-motel")
    public String PostAddMotel(@ModelAttribute ("motelttr") @Valid Motel motel ,BindingResult bindingResult  , @RequestParam("files") MultipartFile[] files ,
    Model model ,RedirectAttributes attributes) {
        return motelImpl.AddMotel(motel, bindingResult, files, model,attributes);
    }
 
    @GetMapping("/admin/manage-motel")
    public String getMethodName(HttpServletRequest request , Model model) {
    return motelImpl.CheckManageMotel(request, model);
    }
    @GetMapping("/admin/manage-motel/{idmotel}")
    public String getMethodName(HttpServletResponse response ,@PathVariable String idmotel, Model model) {
        int idmotel1 = Integer.valueOf(idmotel);
        System.out.println(idmotel1+"dd");
    return motelImpl.AddIdMotelInAccount(response, idmotel1, model);
    }

    @GetMapping("/admin/update-motel")
    public String GetUpdateMotel(Model model){
        return motelImpl.GetUpdateMotel(model);
    }
    @PostMapping("/admin/update-motel")
    public String PostUpdateMotel(@ModelAttribute ("motelttr") @Valid Motel motel ,BindingResult bindingResult  , @RequestParam("files") MultipartFile[] files,
    Model model,RedirectAttributes attributes) {
        return motelImpl.PostUpadateMotel(motel, model, files, bindingResult,attributes);
    }
    @GetMapping("/admin/condition-motel")
    public String getMethodName() {
        return motelImpl.StatusUpdatesMotel();
    }

    @GetMapping("/admin/add-motelroom")
    public String getMethodName22(Model model ,@ModelAttribute ("motelroom") MotelRoom motelRoom) {
      return roomService.GetAddMotelRoom(model);
    }
    @PostMapping("/admin/add-motelroom")
    public String postMethodName(Model model ,@ModelAttribute ("motelroom")  @Valid MotelRoom motelRoom ,BindingResult bindingResult , @RequestParam("files") MultipartFile[] files
    ,RedirectAttributes attributes) {
        return roomService.PostAddMotelRoom(motelRoom, model, files, bindingResult, attributes);
    }
    
  @GetMapping("/admin/manage-motelroom")
  public String getMethodName(Model model) {
      return roomService.ManageMotelRoom(model);
  }
  @GetMapping("/admin/update-motelroom/{id}")
  public String GetUpdateMotelRoom(@PathVariable Integer id ,Model model) {
      return roomService.GetUpdateMotelRoom(model, id);
  }
  
  @PostMapping("/admin/update-motelroom")
  public String PostUpdateMotelRoom(Model model ,@ModelAttribute ("motelroom")  @Valid MotelRoom motelRoom ,BindingResult bindingResult , @RequestParam("files") MultipartFile[] files
  ,RedirectAttributes attributes) {
      return roomService.PostUpdateMotelRoom(motelRoom, model, files, bindingResult, attributes);
  }
  
    
// @PostMapping("/admin/add-demo")
// public String updateMotel(@RequestParam("formType") String formType ,@Valid @ModelAttribute("wifiCash") WifiCash wifiCash, BindingResult wifiCashResult,
// @Valid @ModelAttribute("WaterCash") WaterCash WaterCash, BindingResult WaterCashcashResult,Model model)
//  {
//     if ("form1".equals(formType)) {
//         if (wifiCashResult.hasErrors()) {
//             WifiCash a=new WifiCash();
//             model.addAttribute("wifiCash", a);
//             return "/admin/motel/demo";
//         }
//     } else if ("form2".equals(formType)) {
//         if (WaterCashcashResult.hasErrors()) {
//            return "redirect:/admin/update-motel"+"/1";
//         }
//     } else if ("form3".equals(formType)) {
//         // Xử lý cho form 3
//     }
//     return formType;
// }

    // @GetMapping("/admin/manage-motel/{idmotel}")
    // public String getMethodName1(@PathVariable String idmotel, HttpServletResponse response, Model model) {
    //     int intidmotel = Integer.valueOf(idmotel);
    //    return motelImpl.addcookei(response, intidmotel, model);
    // }
}
