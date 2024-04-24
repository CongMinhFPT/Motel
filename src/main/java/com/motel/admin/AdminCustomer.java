package com.motel.admin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.motel.entity.Role;
import com.motel.repository.AccountsRepository;
import com.motel.repository.AuthorityRepository;
import com.motel.repository.RequestAuthorityRepository;
import com.motel.repository.RoleRepository;
import com.motel.service.AccountService;
import com.motel.service.MailerService;

@MultipartConfig
@Controller
public class AdminCustomer {

	@Autowired
	AccountsRepository accountsRepository;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	@Autowired
	BCryptPasswordEncoder pe;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	MailerService mailerService;
	
	@Autowired
	RequestAuthorityRepository requestAuthorityRepository;
	
	private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
	
	private static Map<String, Account> codeMap = new HashMap<>();
	
	private static String generateCode() {
		return UUID.randomUUID().toString().substring(0,6);
	}
	
	@GetMapping("/customerList")
	public String showList(Model model) {
		List<Account> acc = accountsRepository.findAll();
		Collections.sort(acc, (acc1, acc2) -> {
		    // Lấy trạng thái của tài khoản
		    boolean isActive1 = acc1.isActive();
		    boolean isActive2 = acc2.isActive();
		    
		    // Xét trường hợp tài khoản 1 hoạt động và tài khoản 2 không hoạt động
		    if (isActive1 && !isActive2) {
		        return -1; // Tài khoản 1 được đưa lên đầu danh sách
		    } else if (!isActive1 && isActive2) {
		        return 1; // Tài khoản 2 được đưa lên đầu danh sách
		    }
		    
		    return 0; // Giữ nguyên vị trí của các tài khoản khác
		});

		model.addAttribute("customer",acc);
		int count = requestAuthorityRepository.findRequestCount();
    	model.addAttribute("requestCount", count);
		return "admin/customers/customerList";
	}
	
	@GetMapping("/find")
	public String find(Model model, @RequestParam("find") String find) {
	    List<Account> findEmail = accountsRepository.findByEmail(find);
	    Collections.sort(findEmail, (acc1, acc2) -> {
		    // Lấy trạng thái của tài khoản
		    boolean isActive1 = acc1.isActive();
		    boolean isActive2 = acc2.isActive();
		    
		    // Xét trường hợp tài khoản 1 hoạt động và tài khoản 2 không hoạt động
		    if (isActive1 && !isActive2) {
		        return -1; // Tài khoản 1 được đưa lên đầu danh sách
		    } else if (!isActive1 && isActive2) {
		        return 1; // Tài khoản 2 được đưa lên đầu danh sách
		    }
		    
		    return 0; // Giữ nguyên vị trí của các tài khoản khác
		});
	    if (!findEmail.isEmpty()) {
	        model.addAttribute("customer", findEmail);
	    } else {
	        List<Account> findphone = accountsRepository.findByPhone1(find);
	        Collections.sort(findphone, (acc1, acc2) -> {
			    // Lấy trạng thái của tài khoản
			    boolean isActive1 = acc1.isActive();
			    boolean isActive2 = acc2.isActive();
			    
			    // Xét trường hợp tài khoản 1 hoạt động và tài khoản 2 không hoạt động
			    if (isActive1 && !isActive2) {
			        return -1; // Tài khoản 1 được đưa lên đầu danh sách
			    } else if (!isActive1 && isActive2) {
			        return 1; // Tài khoản 2 được đưa lên đầu danh sách
			    }
			    
			    return 0; // Giữ nguyên vị trí của các tài khoản khác
			});
	        if (!findphone.isEmpty()) {
	            model.addAttribute("customer", findphone);
	        } else {
	            List<Account> findCitizen = accountsRepository.findByCitizen(find);
	            Collections.sort(findCitizen, (acc1, acc2) -> {
				    // Lấy trạng thái của tài khoản
				    boolean isActive1 = acc1.isActive();
				    boolean isActive2 = acc2.isActive();
				    
				    // Xét trường hợp tài khoản 1 hoạt động và tài khoản 2 không hoạt động
				    if (isActive1 && !isActive2) {
				        return -1; // Tài khoản 1 được đưa lên đầu danh sách
				    } else if (!isActive1 && isActive2) {
				        return 1; // Tài khoản 2 được đưa lên đầu danh sách
				    }
				    
				    return 0; // Giữ nguyên vị trí của các tài khoản khác
				});
	            model.addAttribute("customer", findCitizen);
	        }
	    }
	    return "admin/customers/customerList"; 
	}

	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id,Model model) {
		Account acc = accountService.findById(id).orElseThrow(() -> new IllegalArgumentException("Id không tồn tại: " + id));
		acc.setActive(false);
		accountsRepository.save(acc);
		return "redirect:/customerList";
	}
	
	@GetMapping("/add")
	public String create(@ModelAttribute("accounts") Account account){
		return "admin/customers/customerFormCr";
	}
	
	@PostMapping("/add/save")
	public String createSubmit(@Valid @ModelAttribute("accounts") Account account, BindingResult bindingResult, @RequestParam("image") MultipartFile photo, Model model) {
		
		if (bindingResult.hasErrors()) {
			if(photo.isEmpty()) {
				model.addAttribute("photo_message", "Vui lòng chọn ảnh!");
			}
			if (account.getPhone().isBlank()) {
				model.addAttribute("phone", "Vui lòng nhập số điện thoại!");
			}
			if(account.getCitizen().isBlank()) {
				model.addAttribute("citizen", "Vui lòng nhập số căn cước công dân!");
			}
			if (account.getCreateDate() == null) {
				model.addAttribute("date", "Vui lòng chọn ngày tháng năm sinh!");
				
			}
			return "admin/customers/customerFormCr";
		}
		if (!account.getPhone().matches("^(0[2|3|5|7|8|9])+([0-9]{8})")) {
			model.addAttribute("phone", "Sai định dạng số điện thoại!");
			return "admin/customers/customerFormCr";
		}
		
		if (accountsRepository.getByEmail(account.getEmail()) != null) {
			model.addAttribute("email", "Email này đã tồn tại!");
			return "admin/customers/customerFormCr";
		}
		if(accountsRepository.findByPhone(account.getPhone()) != null ) {
			model.addAttribute("phone", "Số điện thoại đã tồn tại. Vui lòng nhập số điện thoại khác!");
			return "admin/customers/customerFormCr";
		}
		if(!account.getCitizen().matches("^\\d{9}|\\d{12}$")) {
			model.addAttribute("citizen", "Sai định dạng số căn cước công dân. Vui lòng nhập 9 hoặc 12 chữ số!");
			return "admin/customers/customerFormCr";
		}
		if(accountsRepository.getByCitizen(account.getCitizen()) != null ) {
			model.addAttribute("citizen", "Số căn cước công dân này đã tồn tại.!");
			return "admin/customers/customerFormCr";
		}
		if (photo != null && !photo.isEmpty()) {
			try {
				String fileName = photo.getOriginalFilename();
				Path fileNameAnPath = Paths.get(UPLOAD_DIRECTORY, fileName);
				Files.write(fileNameAnPath, photo.getBytes());
				account.setAvatar(fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			model.addAttribute("photo_message", "Vui lòng chọn ảnh!");
			return "admin/customers/customerFormCr";
		}
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
			return "admin/customers/customerFormCr";
		}
		
		String confirm = generateCode();
		codeMap.put(confirm, account);
		try {
			sendConfirm(account.getEmail(), confirm);
			model.addAttribute("signupinfo", "Vui lòng xem email để lấy mã xác nhận!");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("signuperr", "Lỗi gửi mã xác nhận!");
		}
		return "admin/customers/customerFormCr";
	}
	
	@GetMapping("/confirmCus")
	public String show() {
		return "admin/customers/confirmCus";
	}
	
	@PostMapping("/add/saveCus")
	public String saveCus(@ModelAttribute("accounts") Account account, @RequestParam("code")String code, Model model) {
		if(code.isBlank()) {
			model.addAttribute("code", "Vui lòng nhập mã xác nhận!");
			return "admin/customers/confirmCus";
		}
		if(codeMap.containsKey(code)) {
			Account confirmCus = codeMap.get(code);
			String passpe = pe.encode(confirmCus.getPassword());
			confirmCus.setPassword(passpe);
			accountsRepository.save(confirmCus);
			
			
			Role staff = roleRepository.findById("CUSTOMER").orElseGet(() -> {
				Role newRole = new Role();
				newRole.setId("CUSTOMER");
				return roleRepository.save(newRole);
			});
			
			Authority au = new Authority();
			au.setAccount(confirmCus);
			au.setRole(staff);
			authorityRepository.save(au);
			mailerService.add(memeMessage -> {
	            MimeMessageHelper helper = new MimeMessageHelper(memeMessage);
	            try {
	                helper.setTo(confirmCus.getEmail());
	                helper.setSubject("Nhà Trọ F.E Xin Chào!");
	                helper.setText("Nhà trọ F.E luôn là lựa chọn tốt nhất. Hân hạnh được phục vụ quí khách! ");
	            } catch (MessagingException e) {
	                e.printStackTrace();
	            }
	        });
			codeMap.remove(code);
			model.addAttribute("signup", "Thêm người dùng thành công!.");
		}else {
	        // Hiển thị thông báo lỗi nếu mã xác nhận không hợp lệ
	        model.addAttribute("signuperr", "Mã xác nhận không hợp lệ.");
	    }
		return "admin/customers/confirmCus";
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
		if(account.getCitizen().isBlank()) {
			model.addAttribute("citizen", "Vui lòng nhập số căn cước công dân!");
			return "admin/customers/customerForm";
		}
		if(!account.getCitizen().matches("^\\d{9}|\\d{12}$")) {
			model.addAttribute("citizen", "Sai định dạng số căn cước công dân. Vui lòng nhập 9 hoặc 12 chữ số!");
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
		
		String confirm = generateCode();
		codeMap.put(confirm, account);
		try {
			sendConfirm(account.getEmail(), confirm);
			model.addAttribute("updateinfo", "Vui lòng xem email để lấy mã xác nhận!");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("updateerr", "Lỗi gửi mã xác nhận!");
		}
		return "admin/customers/customerForm";
	}
	
	@GetMapping("/confirmCusUp")
	public String showUp() {
		return "admin/customers/confirmCusUp";
	}
	
	@PostMapping("/confirmCusUpSave")
	public String saveCusUp(@ModelAttribute("accounts") Account account, @RequestParam("code")String code, Model model) {
		if(code.isBlank()) {
			model.addAttribute("code", "Vui lòng nhập mã xác nhận!");
			return "admin/customers/confirmCusUp";
		}
		if(codeMap.containsKey(code)) {
			Account confirmCus = codeMap.get(code);
			accountsRepository.save(confirmCus);
			codeMap.remove(code);
			model.addAttribute("update", "Cập nhật thành công!.");
		}else {
	        // Hiển thị thông báo lỗi nếu mã xác nhận không hợp lệ
	        model.addAttribute("updateerr", "Mã xác nhận không hợp lệ.");
	    }
		return "admin/customers/confirmCusUp";
	}
	
	private void sendConfirm(String email, String confirm) {
		// gửi email
		mailerService.add(mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			try {
				helper.setTo(email);
				helper.setSubject("Nhà Trọ F.E Xin Chào!");
				helper.setText(
						"Chúng tôi cần xác nhận đây là tài khoản của bạn: <br/>Đây là mã xác nhận của bạn: <span style='font-size: 18px; color: red'>"
								+ confirm + "</span>.",
						true);

			} catch (MessagingException e) {
				e.printStackTrace();
			}
		});
	}
}
