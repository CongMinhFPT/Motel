package com.motel.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.User;
import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.FileManager.FileManager;
import com.motel.entity.Account;
import com.motel.entity.CustomUserDetails;
import com.motel.entity.Motel;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRepository;
import com.motel.service.impl.ManageMotelImpl;

@Service
public class ManageMotelService implements ManageMotelImpl {
    @Autowired
    AccountsRepository account;
    @Autowired
    MotelRepository motelR;
    @Autowired
    FileManager fileManager;

    @Override
    public Optional<CustomUserDetails> CheckLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails details = (CustomUserDetails) principal;
            System.out.println(details.getAccountid() + "idaccount");
            System.out.println(details.getMotelid() + "idmotel");
            return Optional.of(details);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public int GetCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("motelcookie".equals(cookie.getName())) {
                    String giaTriCookie = cookie.getValue();
                    int intgiaTriCookie = Integer.valueOf(giaTriCookie);
                    return intgiaTriCookie;
                }
            }
        }
        return 0;
    }

    @Override
    public void AddCookie(HttpServletResponse response, int idmotel, Model model) {
        String stringValue = String.valueOf(idmotel);
        Cookie cookie = new Cookie("motelcookie", stringValue);
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }

    @Override
    public String CheckManageMotel(HttpServletRequest request, Model model) {
        if (CheckLogin().isPresent()) {
            CustomUserDetails details = CheckLogin().get();
            if (CheckAccountSetIdMotel(details)) {
                SetModelMotel(model);
                List<Motel> motels = account.getById(details.getAccountid()).getMotel();
                model.addAttribute("listmotel", motels);
                System.out.println(1);
                return "admin/motel/manage-motel";
            } else {
                if (GetCookie(request) != 0) {
                    int idmotel = GetCookie(request);
                    if (CheckIdMotelInAccount(idmotel, model)) {
                        SetAccount(idmotel);
                        List<Motel> motels = account.getById(details.getAccountid()).getMotel();
                        model.addAttribute("listmotel", motels);
                        System.out.println(2);
                        return "admin/motel/manage-motel";
                    } else {
                        List<Motel> motels = account.getById(details.getAccountid()).getMotel();
                        model.addAttribute("listmotel", motels);
                        System.out.println(3);
                        return "admin/motel/manage-motel";
                    }
                } else {
                    List<Motel> motels = account.getById(details.getAccountid()).getMotel();
                    model.addAttribute("listmotel", motels);
                    System.out.println(3);
                    return "admin/motel/manage-motel";
                }
            }
        } else {
            return "home/signin";
        }
    }

    @Override
    public Boolean CheckIdMotelInAccount(int idmotel, Model model) {

        CustomUserDetails cDetails = CheckLogin().get();
        List<Motel> listmotel = account.getById(cDetails.getAccountid()).getMotel();
        for (Motel t : listmotel) {
            if (t.getMotelId() == idmotel) {
                model.addAttribute("motel", t);
                return true;
            }
        }

        return false;
    }

    @Override
    public void SetAccount(int idmotel) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails details = (CustomUserDetails) principal;
            CustomUserDetails details2 = new CustomUserDetails(details.getUsername(), "123456789",
                    details.getAuthorities(), details.getAccountid(), idmotel);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(details2,
                    "123456789", details2.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

    }

    @Override
    public Boolean CheckAccountSetIdMotel(CustomUserDetails us) {

        CustomUserDetails details = us;
        if (details.getMotelid() == 0) {
            return false;
        }
        return true;

    }

    @Override
    public void SetModelMotel(Model model) {
        CustomUserDetails cDetails = CheckLogin().get();
        if (CheckAccountSetIdMotel(cDetails)) {
            Motel motel = motelR.getById(cDetails.getMotelid());
            model.addAttribute("motel", motel);

        }
    }

    @Override
    public String AddIdMotelInAccount(HttpServletResponse response, int idmotel, Model model) {
        if (CheckLogin().isPresent()) {
            if (CheckIdMotelInAccount(idmotel, model)) {
                SetAccount(idmotel);
                AddCookie(response, idmotel, model);
                return "redirect:/admin/show-motel";
            } else {
                return "admin/error/error404";
            }
        }
        return "home/signin";
    }

    @Override
    public String AddMotel(Motel motel, BindingResult bindingResult, MultipartFile[] files, Model model,
            RedirectAttributes attributes) {
        if (CheckLogin().isPresent()) {
            CustomUserDetails userDetails = CheckLogin().get();
            if (bindingResult.hasErrors()) {
                SetModelMotel(model);
                return "admin/motel/add-motel";
            }
            if (files != null && files.length > 0 && !files[0].isEmpty()) {
                Motel motel2 = motel;
                String nameimg = ImgSave("ImgMotel", files);
                motel2.setImage(nameimg);
                Account account1 = account.getById(userDetails.getAccountid());
                motel2.setAccount(account1);
                motelR.save(motel2);
                SetModelMotel(model);
                attributes.addFlashAttribute("successMessage", "Thêm thành công!");
                return "redirect:/admin/manage-motel";
            } else {
                Motel motel2 = motel;
                String nameimg = "img-defaul.png";
                motel2.setImage(nameimg);
                Account account1 = account.getById(userDetails.getAccountid());
                motel2.setAccount(account1);
                motelR.save(motel2);
                SetModelMotel(model);
                attributes.addFlashAttribute("successMessage", "Thêm thành công!");
                return "redirect:/admin/manage-motel";
            }
        }
        return "home/signin";
    }

    @Override
    public String ImgSave(String folder, MultipartFile[] files) {
        List<String> list = new ArrayList<>();
        list = fileManager.save(folder, files);
        return list.get(0);
    }

    @Override
    public String GetMotel(Model model) {
        if (CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = CheckLogin().get();
            if (CheckAccountSetIdMotel(customUserDetails)) {
                SetModelMotel(model);
            }
            return "admin/motel/add-motel";
        }
        return "home/signin";
    }

    @Override
    public String ShowMotel(Model model) {
        if (CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = CheckLogin().get();
            if (CheckAccountSetIdMotel(customUserDetails)) {
                SetModelMotel(model);
                return "admin/motel/home-motel";
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

    @Override
    public String GetUpdateMotel(Model model) {
        if (CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = CheckLogin().get();
            if (CheckAccountSetIdMotel(customUserDetails)) {
                Motel motel = motelR.getById(customUserDetails.getMotelid());
                SetModelMotel(model);
                model.addAttribute("motelttr", motel);
                return "admin/motel/update-motel";
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

    @Override
    public String PostUpadateMotel(Motel motel, Model model, MultipartFile[] files, BindingResult bindingResult,
            RedirectAttributes attributes) {
        if (CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = CheckLogin().get();
            if (CheckAccountSetIdMotel(customUserDetails)) {
                if (bindingResult.hasErrors()) {
                    SetModelMotel(model);
                    return "admin/motel/add-motel";
                }
                if (files != null && files.length > 0 && !files[0].isEmpty()) {
                    Motel motel2 = motelR.getById(customUserDetails.getMotelid());
                    String CheckNullImg = motel2.getImage();
                    if (CheckNullImg != null) {
                        if (!CheckNullImg.equals("img-defaul.png")) {
                            fileManager.delete("ImgMotel", CheckNullImg);
                        }
                    }
                    String nameimg = ImgSave("ImgMotel", files);
                    int idmotel = motel2.getMotelId();
                    Account account = motel2.getAccount();
                    Date day = motel2.getCreateDate();
                    Boolean stasus =motel2.isStatus();
                    motel2 = motel;
                    motel2.setMotelId(idmotel);
                    motel2.setImage(nameimg);
                    motel2.setAccount(account);
                    motel2.setCreateDate(day);
                    motel2.setStatus(stasus);
                    motelR.save(motel2);
                    attributes.addFlashAttribute("successMessageUpdate", "Cập nhật thành công!");
                    return "redirect:/admin/update-motel";
                } else {
                    Motel motel2 = motelR.getById(customUserDetails.getMotelid());
                    String nameimg = motel2.getImage();
                    int idmotel = motel2.getMotelId();
                    Account account = motel2.getAccount();
                    Date day = motel2.getCreateDate();
                    Boolean stasus =motel2.isStatus();
                    motel2 = motel;
                    motel2.setMotelId(idmotel);
                    motel2.setImage(nameimg);
                    motel2.setAccount(account);
                    motel2.setCreateDate(day);
                    motel2.setStatus(stasus);
                    motelR.save(motel2);
                    attributes.addFlashAttribute("successMessageUpdate", "Cập nhật thành công!");
                    return "redirect:/admin/update-motel";
                }
            } else {
                return "redirect:/admin/manage-motel";
            }
        } else {
            return "home/signin";
        }
    }

    @Override
    public String StatusUpdatesMotel() {
        if (CheckLogin().isPresent()) {
            CustomUserDetails customUserDetails = CheckLogin().get();
            if (CheckAccountSetIdMotel(customUserDetails)) {
               Motel motel = motelR.getById(customUserDetails.getMotelid());
               if (motel.isStatus()) {
                motel.setStatus(false);
                motelR.save(motel);
                return "redirect:/admin/show-motel";
               }
               motel.setStatus(true);
               motelR.save(motel);
             return  "redirect:/admin/show-motel";
            }else{
                return "redirect:/admin/manage-motel";
            }
        }
        return "home/signin";
    }
}