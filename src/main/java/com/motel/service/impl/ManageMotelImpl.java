package com.motel.service.impl;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.motel.entity.Account;
import com.motel.entity.CustomUserDetails;
import com.motel.entity.Motel;

@Service
public interface ManageMotelImpl {
   public Optional<CustomUserDetails> checklogin();

   public int getcookei(HttpServletRequest request);

   public void addcookei(HttpServletResponse response , int idmotel ,Model model);

   String checkManageMotel(HttpServletRequest request,Model model);

   void setAccount(int idmotel  );
   void setModelMotel(Model model );
  
   public Boolean checklidmotel(int idmotel ,Model model);
   public Boolean checkmotelaccount(CustomUserDetails us);
   public String ManageMotelPava(HttpServletResponse response , int idmotel ,Model model);
} 