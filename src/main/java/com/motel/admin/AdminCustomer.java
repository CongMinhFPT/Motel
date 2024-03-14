package com.motel.admin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.motel.entity.Account;
import com.motel.entity.Authority;
import com.motel.repository.AccountsRepository;
import com.motel.repository.AuthorityRepository;
import com.motel.service.AccountService;

@MultipartConfig
@Controller
public class AdminCustomer {

	@Autowired
	AccountsRepository accountsRepository;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AuthorityRepository authorityRepository;
	private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
	
	@GetMapping("/customerList")
	public String showList(Model model) {
		List<Account> acc = accountsRepository.findAll();
		model.addAttribute("customer",acc);
		return "admin/customers/customerList";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id,Model model) {
		Account acc = accountService.findById(id).orElseThrow(() -> new IllegalArgumentException("Id không tồn tại: " + id));
		acc.setActive(false);
		accountsRepository.save(acc);
		return "redirect:/customerList";
	}
	
	@GetMapping("/edit/{id}")
	public String form(Model model, @PathVariable("id") Integer id) {
		Account cus = accountService.getById(id);
		model.addAttribute("account", cus);
		return "admin/customers/customerForm";
	}
	
	@PostMapping("/edit/{accountId}")
	public String submit(@PathVariable("accountId") Integer accountId, Model model, @ModelAttribute("account") Account account, @RequestParam("image") MultipartFile photo) {
		Account accountCrr = accountService.getById(accountId);
		if(photo != null && !photo.isEmpty()) {
			try {
				String fileName = photo.getOriginalFilename();
				Path fileNamePath = Paths.get(UPLOAD_DIRECTORY, fileName);
				Files.write(fileNamePath, photo.getBytes());
				account.setAvatar(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			String photoName = accountCrr.getAvatar();
			account.setAvatar(photoName);
		}
		if (account.getFullname().isBlank()) {
			model.addAttribute("fullname", "Vui lòng nhập họ và tên!");
			return "admin/customers/customerForm";
		}
		if (account.getPhone().isBlank()) {
			model.addAttribute("phone", "Vui lòng nhập số điện thoại!");
			return "admin/customers/customerForm";
		}
		if (!account.getPhone().matches("^(0[2|3|5|7|8|9])+([0-9]{8})")) {
			model.addAttribute("phone", "Sai định dạng số điện thoại!");
			return "admin/customers/customerForm";
		}
		if (account.getCreateDate() == null) {
			model.addAttribute("date", "Vui lòng chọn ngày tháng năm sinh!");
			return "admin/customers/customerForm";
		} else {
			Calendar calNow = Calendar.getInstance(); // lấy thời gian hiện tại
			System.out.println("calNow>> " + calNow);
			Calendar calBirth = Calendar.getInstance(); // lấy ngày tháng năm sinh người dùng
			System.out.println("calBirth>> " + calBirth);
			calBirth.setTime(account.getCreateDate()); // đặc thời gian calBirth bằng ngày tháng năm sinh người dùng
			int age = calNow.get(Calendar.YEAR) - calBirth.get(Calendar.YEAR); // năm hiện tại trừ năm sinh
			System.out.println("age>> " + age);
			if (calNow.get(Calendar.DAY_OF_YEAR) < calBirth.get(Calendar.DAY_OF_YEAR)) {
				age--; // Kiểm tra xem ngày hiện tại có nhỏ hơn ngày sinh của người dùng không. Nếu có,
						// chứng tỏ họ chưa đủ tuổi trong năm nay, vì vậy ta giảm tuổi đi một năm.
				System.out.println("ageif>> " + age);
			}
			if (age < 18) { // Kiểm tra xem tuổi của người dùng có dưới 18 tuổi không. Nếu có, ta thêm thông
							// báo lỗi vào model và trả về trang thông tin.
				model.addAttribute("date", "Bạn phải đủ 18 tuổi trở lên!");
				return "admin/customers/customerForm";
			}
		}
		String email = accountCrr.getEmail();
		String pass = accountCrr.getPassword();
		System.out.println("pass>> " + pass);
		account.setEmail(email);
		account.setPassword(pass);
		
		accountsRepository.save(account);
		model.addAttribute("update", "Cập nhật thành công!");
		return "admin/customers/customerForm";
	}
}
