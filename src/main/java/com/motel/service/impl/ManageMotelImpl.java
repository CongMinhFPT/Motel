package com.motel.service.impl;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.Account;
import com.motel.entity.CustomUserDetails;
import com.motel.entity.Motel;

@Service
public interface ManageMotelImpl {
   public Optional<CustomUserDetails> CheckLogin();

   public int GetCookie(HttpServletRequest request);

   public void AddCookie(HttpServletResponse response , int idmotel ,Model model);

   String CheckManageMotel(HttpServletRequest request,Model model);

   void SetAccount(int idmotel  );
   void SetModelMotel(Model model );
  
   public Boolean CheckIdMotelInAccount(int idmotel ,Model model);
   public Boolean CheckAccountSetIdMotel(CustomUserDetails us);
   public String AddIdMotelInAccount(HttpServletResponse response , int idmotel ,Model model);
   public String AddMotel(Motel motel ,BindingResult bindingResult ,  MultipartFile[] files , Model model,RedirectAttributes attributes);
   public String ImgSave( String namefolder,MultipartFile[] files);
   public String GetMotel(Model model);
} 