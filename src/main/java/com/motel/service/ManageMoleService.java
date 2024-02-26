package com.motel.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import com.motel.entity.CustomUserDetails;
import com.motel.entity.Motel;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRepository;
import com.motel.service.impl.ManageMotelImpl;

@Service
public class ManageMoleService implements ManageMotelImpl {
    @Autowired
    AccountsRepository account;
    @Autowired
    MotelRepository motelR;

    @Override
    public Optional<CustomUserDetails> checklogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails details = (CustomUserDetails) principal;
            return Optional.of(details);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public int getcookei(HttpServletRequest request) {
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
    public void addcookei(HttpServletResponse response, int idmotel, Model model) {
        String stringValue = String.valueOf(idmotel);
        Cookie cookie = new Cookie("motelcookie", stringValue);
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }

    @Override
    public String checkManageMotel(HttpServletRequest request, Model model) {
        if (checklogin().isPresent()) {
            CustomUserDetails details = checklogin().get();
            if (checkmotelaccount(details)) {
                setModelMotel(model);
                List<Motel> motels = account.getById(details.getAccountid()).getMotel();
                model.addAttribute("listmotel", motels);
                System.out.println(1);
                return "admin/motel/manage-motel";
            } else {
                if (getcookei(request) != 0) {
                    int idmotel = getcookei(request);
                    if (checklidmotel(idmotel, model)) {
                        setAccount(idmotel);
                        List<Motel> motels = account.getById(details.getAccountid()).getMotel();
                        model.addAttribute("listmotel", motels);
                        System.out.println(2);
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
            return "home/news";
        }
        return "home/news";
    }

    @Override
    public Boolean checklidmotel(int idmotel, Model model) {

        CustomUserDetails cDetails = checklogin().get();
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
    public void setAccount(int idmotel) {
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
    public Boolean checkmotelaccount(CustomUserDetails us) {

        CustomUserDetails details = us;
        if (details.getMotelid() == 0) {
            System.out.println(details.getMotelid()+"loia");
            return false;
        }
        System.out.println(details.getMotelid()+"trana");
        return true;

    }

    @Override
    public void setModelMotel(Model model) {

        CustomUserDetails cDetails = checklogin().get();
        Motel motel = motelR.getById(cDetails.getMotelid());
        model.addAttribute("motel", motel);

    }

    @Override
    public String ManageMotelPava(HttpServletResponse response, int idmotel, Model model) {
        if (checklogin().isPresent()) {
            if (checklidmotel(idmotel, model)) {
                setAccount(idmotel);
                addcookei(response, idmotel, model);
                return "admin/motel/manage-motel";
            } else {
                return "home/news";
            }
        }
        return "home/news";
    }
}