package com.motel.admin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRepository;
import com.motel.repository.RenterRepository;
import com.motel.repository.RequestAuthorityRepository;
import com.motel.entity.Account;
import com.motel.entity.CustomUserDetails;
import com.motel.entity.Motel;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.service.RenterService;
import com.motel.service.impl.ManageMotelImpl;

@Controller
public class AdminController {

	@Autowired
	RenterService renterService;
	@Autowired
	RequestAuthorityRepository requestAuthorityRepository;

	@Autowired
	RenterRepository renterRepository;

	@Autowired
	AccountsRepository accountsRepository;

	@Autowired
	ManageMotelImpl manageMotelImpl;

	@Autowired
	MotelRepository motelRepository;

	@GetMapping("/admin")
	public String index(Model model, Authentication authentication) {
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

				manageMotelImpl.SetModelMotel(model);
				model.addAttribute("renters", renters.size());
				String emailAccount = authentication.getName();
				Account account = accountsRepository.getByEmail(emailAccount);
				model.addAttribute("accountIdStatistic", account.getAccountId());
				int count = requestAuthorityRepository.findRequestCount();
				model.addAttribute("requestCount", count);

				return "admin/home/index";
			} else {
				return "redirect:/admin/manage-motel";
			}
		} else {
			return "home/signin";
		}
	}

	@GetMapping("/authority")
	public String authority(Model model) {
		int count = requestAuthorityRepository.findRequestCount();
		model.addAttribute("requestCount", count);
		manageMotelImpl.SetModelMotel(model);
		return "admin/authority/auth";
	}

	@GetMapping("/admin/statistic")
	public String statistic(Model model, Authentication authentication) {
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

				manageMotelImpl.SetModelMotel(model);
				model.addAttribute("renters", renters.size());
				String emailAccount = authentication.getName();
				Account account = accountsRepository.getByEmail(emailAccount);
				model.addAttribute("accountIdStatistic", account.getAccountId());
				return "admin/home/index";
			} else {
				return "redirect:/admin/manage-motel";
			}
		} else {
			return "home/signin";
		}
	}
}
