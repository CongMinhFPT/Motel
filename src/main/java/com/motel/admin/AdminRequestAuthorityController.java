package com.motel.admin;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.motel.entity.Account;
import com.motel.entity.RequestAuthority;
import com.motel.entity.RequestAuthorityStatus;
import com.motel.repository.AccountsRepository;
import com.motel.repository.RequestAuthorityRepository;
import com.motel.repository.RequestAuthorityStatusRepository;
import com.motel.service.MailerService;
import com.motel.service.impl.ManageMotelImpl;

@Controller
public class AdminRequestAuthorityController {

	@Autowired
	RequestAuthorityRepository requestAuthorityRepository;

	@Autowired
	RequestAuthorityStatusRepository requestAuthorityStatusRepository;

	@Autowired
	AccountsRepository accountsRepository;

	@Autowired
	MailerService mailerService;

	@Autowired
	ManageMotelImpl manageMotelImpl;

	@GetMapping("/requestauth")
	public String show(Model model) {
		List<RequestAuthority> re = requestAuthorityRepository.findRequestadmin();
		Collections.sort(re, (r1, r2) -> {
			if (r1.getRequestAuthorityStatus().getRequestAuthorityStatusId() == 1) {
				return -1; // r1 được đưa lên đầu danh sách
			} else if (r2.getRequestAuthorityStatus().getRequestAuthorityStatusId() == 1) {
				return 1; // r2 được đưa lên đầu danh sách
			}
			return 0; // Giữ nguyên vị trí của các yêu cầu khác
		});
		model.addAttribute("request", re);
		int count = requestAuthorityRepository.findRequestCount();
		model.addAttribute("requestCount", count);
		manageMotelImpl.SetModelMotel(model);
		return "admin/authority/requestAuthority";
	}

	@GetMapping("/editrequest/{id}")
	public String submit(@PathVariable("id") Integer id, Model model) {
		RequestAuthority req = requestAuthorityRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Username không tồn tại: " + id));
		model.addAttribute("request", req);
		manageMotelImpl.SetModelMotel(model);
		return "admin/authority/requestAuthorityFrom";
	}

	@PostMapping("/requestauth/{requestAuthorityId}")
	public String save(@PathVariable("requestAuthorityId") Integer id,
			@ModelAttribute("request") RequestAuthority requestAuthority, Model model) {
		// Lấy RequestAuthority từ Repository
		RequestAuthority req = requestAuthorityRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu với ID: " + id));

		// Lấy ID của RequestAuthorityStatus từ form
		Integer requestAuthorityStatusId = requestAuthority.getRequestAuthorityStatus().getRequestAuthorityStatusId();
		if (requestAuthority.getRequestAuthorityStatus().getRequestAuthorityStatusId() == null) {
			model.addAttribute("sta", "Vui lòng chọn phản hồi!");
			return "admin/authority/requestAuthorityFrom";
		}
		// Kiểm tra xem RequestAuthorityStatus có tồn tại không
		RequestAuthorityStatus reS = requestAuthorityStatusRepository.findById(requestAuthorityStatusId)
				.orElseThrow(() -> new IllegalArgumentException(
						"Không tìm thấy trạng thái yêu cầu với ID: " + requestAuthorityStatusId));

		String respdes = requestAuthority.getRespdescriptions();

		if (respdes.isBlank()) {
			model.addAttribute("des", "Vui lòng nhập phản hồi!");
			return "admin/authority/requestAuthorityFrom";
		}
		// Lấy các thông tin khác từ đối tượng gốc
		Account acc = req.getAccount();
		String des = req.getDescriptions();
		String email = acc.getEmail();

		// Cập nhật các trường của requestAuthority
		requestAuthority.setDescriptions(des);
		requestAuthority.setAccount(acc);
		requestAuthority.setResponseDate(new Date());
		requestAuthority.setRequestAuthorityStatus(reS);

		sendMail(email, respdes);

		// Lưu RequestAuthority đã cập nhật
		requestAuthorityRepository.save(requestAuthority);
		model.addAttribute("create", "Phản hồi thành công!");
		return "admin/authority/requestAuthorityFrom";
	}

	@ModelAttribute("status")
	public Iterable<RequestAuthorityStatus> getStatus() {
		return requestAuthorityStatusRepository.findAllStatus();
	}

	private void sendMail(String email, String desp) {
		mailerService.add(mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			try {
				helper.setTo(email);
				helper.setSubject("Nhà Trọ F.E Xin Chào!");
				helper.setText(desp);
			} catch (Exception e) {

			}
		});
	}
}
