package com.motel.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.motel.entity.Account;
import com.motel.entity.RequestAuthority;
import com.motel.entity.RequestAuthorityStatus;
import com.motel.repository.AccountsRepository;
import com.motel.repository.RequestAuthorityRepository;
import com.motel.repository.RequestAuthorityStatusRepository;
import com.motel.service.RequestAuthorityService;


@Controller
public class RequestAuthorityController {
	
	@Autowired
	AccountsRepository accountsRepository;
	
	@Autowired
	RequestAuthorityRepository requestAuthorityRepository;
	
	@Autowired 
	RequestAuthorityStatusRepository requestAuthorityStatusRepository;

	@Autowired
	RequestAuthorityService requestAuthorityService;
	
	@GetMapping("/requestList")
	public String showList(Model model, Authentication authentication) {
		String email = authentication.getName();
		
		List<RequestAuthority> re = requestAuthorityRepository.findRequest(email);
		Collections.sort(re, (r1, r2) -> {
			if (r1.getRequestAuthorityStatus().getRequestAuthorityStatusId() == 1) {
                return -1; // r1 được đưa lên đầu danh sách
            } else if (r2.getRequestAuthorityStatus().getRequestAuthorityStatusId() == 1) {
                return 1; // r2 được đưa lên đầu danh sách
            }
            return 0; // Giữ nguyên vị trí của các yêu cầu khác
		});
		model.addAttribute("request", re);
		return "home/requestAuthorityList";
	}
	
	@GetMapping("/deleteRe/{id}")
	public String delete(@PathVariable("id") Integer id, Model model ) {
		RequestAuthority re = requestAuthorityService.findById(id).orElseThrow(() -> new IllegalArgumentException("Id không tồn tại: " + id));
		RequestAuthorityStatus res = requestAuthorityStatusRepository.getById(4);
		re.setRequestAuthorityStatus(res);
		requestAuthorityRepository.save(re);
		return"redirect:/requestList";
	}
	@GetMapping("/request")
	public String show(Model model, @ModelAttribute("request") RequestAuthority requestAuthority, Authentication authentication) {
		String id = authentication.getName();
		model.addAttribute("id", id);
		return "home/requestAuthority";
	}
	
	@PostMapping("/request/save")
	public String submit(Model model,@Valid @ModelAttribute("request") RequestAuthority requestAuthority, BindingResult bindingResult, Authentication authentication) {
		
		String id =authentication.getName();
		Account acc = accountsRepository.getByEmail(id);
		Integer requestAuthorityStatusId = 1;
		if(bindingResult.hasErrors()) {
			model.addAttribute("id", id);
			return "home/requestAuthority";
		}
		 // Kiểm tra xem có bản ghi nào với requestAuthorityStatusId là 1 và email tương ứng hay không
        boolean existingRequest = requestAuthorityRepository.existsByRequestAuthorityStatusRequestAuthorityStatusIdAndAccountAccountId(requestAuthorityStatusId, acc.getAccountId());
        if (existingRequest) {
            model.addAttribute("req", "Bạn đã gửi yêu cầu trước đó, nếu bạn muốn gửi lại yêu cầu mới chúng tôi sẽ hủy yêu cầu trước đó của bạn!");
            model.addAttribute("id", id);
            return "home/requestAuthority"; // Điều hướng đến trang hiển thị thông báo lỗi
        }
        
		
		RequestAuthorityStatus re = requestAuthorityStatusRepository.getById(requestAuthorityStatusId);
		
		requestAuthority.setAccount(acc);
		requestAuthority.setRequestAuthorityStatus(re);
		System.out.println("re>>" + re);
		requestAuthorityRepository.save(requestAuthority);
		
		model.addAttribute("creates", "Gửi yêu cầu thành công!");
		List<RequestAuthority> req = requestAuthorityRepository.findRequest(id);
		Collections.sort(req, (r1, r2) -> {
			if (r1.getRequestAuthorityStatus().getRequestAuthorityStatusId() == 1) {
                return -1; // r1 được đưa lên đầu danh sách
            } else if (r2.getRequestAuthorityStatus().getRequestAuthorityStatusId() == 1) {
                return 1; // r2 được đưa lên đầu danh sách
            }
            return 0; // Giữ nguyên vị trí của các yêu cầu khác
		});
		model.addAttribute("request", req);
		return "home/requestAuthorityList";
	}
	
	
	public void deleteRequest(Integer id) {
		RequestAuthority re = requestAuthorityRepository.getById(id);
		RequestAuthorityStatus res = requestAuthorityStatusRepository.getById(4);
		re.setRequestAuthorityStatus(res);
		requestAuthorityRepository.save(re);
	}

	
	@GetMapping("/request/update")
	public String updateRequest(Model model,@ModelAttribute("request") RequestAuthority requestAuthority, Authentication authentication) {
	    String id = authentication.getName();
	    Account acc = accountsRepository.getByEmail(id);
	    model.addAttribute("id", id);
	    // Xóa yêu cầu cũ nếu có
	    List<RequestAuthority> re = requestAuthorityRepository.findByRequestAuthorityId(acc.getAccountId());
	    if (re != null) {
	        for (RequestAuthority ra : re) {
	            Integer requestId = ra.getRequestAuthorityId();
	            System.out.println("Deleting request with ID: " + requestId);
	            deleteRequest(requestId);
	        }
	    }
	    return "home/requestAuthority";
	}

}
