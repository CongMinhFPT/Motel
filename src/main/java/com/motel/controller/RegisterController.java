package com.motel.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.swing.text.Style;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.motel.entity.Account;
import com.motel.entity.Authority;
import com.motel.entity.ChangePassword;
import com.motel.entity.Invoice;
import com.motel.entity.Role;
import com.motel.repository.AccountsRepository;
import com.motel.repository.AuthorityRepository;
import com.motel.repository.InvoiceRepository;
import com.motel.repository.InvoiceStatusRepository;
import com.motel.repository.RoleRepository;
import com.motel.service.AccountService;
import com.motel.service.AuthorityService;
import com.motel.service.MailerService;
import com.motel.service.impl.ManageMotelImpl;

@MultipartConfig
@Controller
public class RegisterController {

	@Autowired
	AccountsRepository accountsRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	MailerService mailerService;

	@Autowired
	HttpSession session;

	@Autowired
	AuthorityService authorityService;

	@Autowired
	BCryptPasswordEncoder pe;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	InvoiceStatusRepository invoiceStatusRepository;

	@Autowired
	ManageMotelImpl filemanage;

	private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

	private static Map<String, Account> codeMap = new HashMap<>();
	private static Map<String, String> confirmationCodes = new HashMap<>();

	private static String generateCode() {
		return UUID.randomUUID().toString().substring(0, 6);
	}

	@GetMapping("/signup")
	public String Signup(@ModelAttribute("accounts") Account account) {
		return "home/signup";
	}

	@PostMapping("/register/save")
	public String save(@Validated @ModelAttribute("accounts") Account account, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			if (account.getPhone().isBlank()) {
				model.addAttribute("phone", "Vui lòng nhập số điện thoại!");
			}
			if (account.getCitizen().isBlank()) {
				model.addAttribute("citizen", "Vui lòng nhập số căn cước công dân!");
			}
			if (account.getCreateDate() == null) {
				model.addAttribute("date", "Vui lòng chọn ngày tháng năm sinh!");

			}
			return "home/signup";
		}

		if (!account.getPhone().matches("^(0[2|3|5|7|8|9])+([0-9]{8})")) {
			model.addAttribute("phone", "Sai định dạng số điện thoại!");
			return "home/signup";
		}

		if (accountsRepository.getByEmail(account.getEmail()) != null) {
			model.addAttribute("email", "Email này đã tồn tại!");
			return "home/signup";
		}
		if (accountsRepository.findByPhone(account.getPhone()) != null) {
			System.out.println(account.getPhone());
			model.addAttribute("phone", "Số điện thoại đã tồn tại. Vui lòng nhập số điện thoại khác!");
			return "home/signup";
		}
		if (!account.getCitizen().matches("^\\d{9}|\\d{12}$")) {
			model.addAttribute("citizen", "Sai định dạng số căn cước công dân. Vui lòng nhập 9 hoặc 12 chữ số!");
			return "home/signup";
		}
		if (accountsRepository.getByCitizen(account.getCitizen()) != null) {
			model.addAttribute("citizen", "Số căn cước công dân này đã tồn tại.!");
			return "home/signup";
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
			return "home/signup";
		}

		String confirmationCode = generateCode();
		codeMap.put(confirmationCode, account);
		System.out.println("Mã xác nhận: " + confirmationCode);
		try {
			sendConfirm(account.getEmail(), confirmationCode);
			model.addAttribute("signupinfo", "Vui lòng xem email để lấy mã xác nhận!");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("signuperr", "Lỗi gửi mã xác nhận!");
		}

		return "home/signup";
	}

	@GetMapping("/confirmSignup")
	public String show() {
		return "home/confirmSignup";
	}

	@PostMapping("/confirmSignup/save")
	public String confirmSave(@ModelAttribute("account") Account account, @RequestParam("code") String code,
			Model model) {

		if (code.isBlank()) {
			model.addAttribute("code", "Vui lòng nhập mã xác nhận!");
			return "home/confirmSignup";
		}

		if (codeMap.containsKey(code)) {
			Account confirmAccount = codeMap.get(code);
			String passpe = pe.encode(confirmAccount.getPassword());
			System.out.println("PassPE>> " + passpe);
			confirmAccount.setPassword(passpe);
			accountsRepository.save(confirmAccount);

			Role staff = roleRepository.findById("CUSTOMER").orElseGet(() -> {
				Role newRole = new Role();
				newRole.setId("CUSTOMER");
				return roleRepository.save(newRole);
			});

			Authority au = new Authority();
			au.setAccount(confirmAccount);
			au.setRole(staff);
			authorityRepository.save(au);

			mailerService.add(memeMessage -> {
				MimeMessageHelper helper = new MimeMessageHelper(memeMessage);
				try {
					helper.setTo(confirmAccount.getEmail());
					helper.setSubject("Nhà Trọ F.E Xin Chào!");
					helper.setText("Nhà trọ F.E luôn là lựa chọn tốt nhất. Hân hạnh được phục vụ quí khách! ");
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			});

			codeMap.remove(code);
			model.addAttribute("signup", "Đăng ký thành công!.");
		} else {
			// Hiển thị thông báo lỗi nếu mã xác nhận không hợp lệ
			model.addAttribute("signuperr", "Mã xác nhận không hợp lệ.");
		}
		return "home/confirmSignup";
	}

	@RequestMapping("/signin")
	public String error(Model model, @RequestParam(required = false) String error) {
		if (error != null) {
			model.addAttribute("error", "Đăng nhập thất bại!");
		}

		return "home/signin";

	}

	@RequestMapping("/auth/access/denied")
	public String denied(Model model) {
		model.addAttribute("errors", "Bạn không có quyền truy cập!");
		return "home/signin";
	}

	@PostMapping("/signin/save")
	public String sigin(Model model, @ModelAttribute("accounts") Account account) {
		Account currentUser = accountsRepository.getByEmail(account.getEmail());
		if (currentUser != null) {
			session.setAttribute("currentUsername", currentUser.getEmail());

		}

		session.setAttribute("user", currentUser);
		System.out.println("user");
		model.addAttribute("signin", "Đăng nhập thành công!");
		return "home/signin";
	}

	@RequestMapping("/oauth2/login/success")
	public String success(OAuth2AuthenticationToken oauth2, Model model) {
		authorityService.loginFromOAuth2(oauth2);
		OAuth2User oauth2User = oauth2.getPrincipal();
		String email = oauth2User.getAttribute("email");
		System.out.println("Email>> " + email);

		Account existingAccount = accountsRepository.getByEmail(email);
		if (existingAccount != null) {
			// Nếu tài khoản đã tồn tại, không cần tạo mới, sử dụng tài khoản hiện có để
			// đăng nhập
			model.addAttribute("auth", "Đăng nhập thành công!");
			return "redirect:/index";
		}

		// Nếu tài khoản chưa tồn tại, tạo một tài khoản mới và lưu vào CSDL
		String generatedString = randompassword();
		System.out.println("pass>> " + generatedString);
		Account acc = new Account();
		acc.setEmail(email);
		acc.setFullname(email);
		acc.setPassword(pe.encode(generatedString));
		acc.setActive(true);
		accountsRepository.save(acc);

		// Tạo quyền cho tài khoản mới
		Role customerRole = roleRepository.findById("CUSTOMER").orElseGet(() -> {
			Role newRole = new Role();
			newRole.setId("CUSTOMER");
			return roleRepository.save(newRole);
		});
		Authority au = new Authority();
		au.setAccount(acc);
		au.setRole(customerRole);
		authorityRepository.save(au);

		model.addAttribute("auth", "Đăng nhập thành công!");
		return "redirect:/index";
	}

	@GetMapping("/change")
	public String Change(Model model, @ModelAttribute("changepass") ChangePassword changepass,
			Authentication authentication) {
		String id = authentication.getName();
		System.out.println("id_ChangeGet>> " + id);
		model.addAttribute("id", id);
		return "home/change_password";
	}

	@PostMapping("/change/save")
	public String changesubmit(Model model, @ModelAttribute("changepass") @Valid ChangePassword changepass,
			BindingResult bindingResult, Authentication authentication) {
		String id = authentication.getName();
		Account acc = accountsRepository.getByEmail(id);
		if (bindingResult.hasErrors()) {
			model.addAttribute("id", id);
			return "home/change_password";
		}
		model.addAttribute("id", id);
		String oldPasswordFormUser = changepass.getOldPassword();
		System.out.println("Mật khẩu cũ: " + oldPasswordFormUser);
		String oldPasswordFromData = acc.getPassword();
		System.out.println("Mật khẩu data: " + oldPasswordFromData);
		System.out.println("Length User: " + oldPasswordFormUser.length());
		System.out.println("Length Database: " + oldPasswordFromData.length());
		// kiểm tra mật khẩu có bằng không
		System.out.println("MK có bằng không: " + oldPasswordFormUser.equals(oldPasswordFromData));
		oldPasswordFormUser = oldPasswordFormUser.trim();
		System.out.println("Equal after trimming: " + oldPasswordFormUser.equals(oldPasswordFromData));
		if (!pe.matches(oldPasswordFormUser, oldPasswordFromData)) {
			model.addAttribute("change", "Mật khẩu hiện tại không đúng!");
			return "home/change_password";
		}
		if (!changepass.getNewPassword().equals(changepass.getConfirmPassword())) {
			model.addAttribute("change", "Xác nhận mật khẩu không đúng!");
			return "home/change_password";
		}
		if (changepass.getNewPassword().equals(changepass.getOldPassword())) {
			model.addAttribute("change", "Mật khẩu mới không được trùng với mật khẩu hiện tại!");
			return "home/change_password";
		}

		String pass = changepass.getNewPassword();
		System.out.println("pass>> " + pass);
		String passpe = pe.encode(pass);
		System.out.println("passpe>> " + passpe);
		acc.setPassword(passpe);
		accountsRepository.save(acc);
		model.addAttribute("pass", "Thay đổi mật khẩu thành công!");
		return "home/change_password";
	}

	@GetMapping("/forgot")
	public String Forgot() {
		return "home/forgot_password";
	}

	@PostMapping("/forgot/save")
	public String ForgotSubmit(Model model, @RequestParam("email") String email) {
		Account currentAcc = accountsRepository.getByEmail(email);
		Integer id = (currentAcc != null) ? currentAcc.getAccountId() : null;
		if (email.isBlank()) {
			model.addAttribute("email", "Vui lòng nhập email!");
			return "home/forgot_password";
		}
		if (id != null) {
			String confirmforgot = generateCode();
			confirmationCodes.put(email, confirmforgot);
			try {
				sendEmail(email, confirmforgot);
				System.out.println("Random forgot:>> " + confirmforgot);
				model.addAttribute("mes", "Vui xem email lấy mã xác nhận!");
				model.addAttribute("email1", email);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("messagee", "Đã có lỗi xảy ra khi gửi email xác nhận!");
			}
		} else {
			model.addAttribute("messagee", "Email không tồn tại!");
			return "home/forgot_password";
		}
		return "home/forgot_password";
	}

	@GetMapping("/confirmForm")
	public String show(Model model, @RequestParam("email") String email) {
		Account acc = accountsRepository.getByEmail(email);
		model.addAttribute("acc", acc);
		model.addAttribute("email", email);
		return "home/confirmgenerate";
	}

	@PostMapping("/confirm/{email}")
	public String Confirm(Model model, @ModelAttribute("acc") Account acount, @RequestParam("code") String code,
			@PathVariable("email") String email) {
		if (confirmationCodes.containsKey(email) && confirmationCodes.get(email).equals(code)) {
			String generatedString = randompassword();
			System.out.println("Random forgot:>> " + generatedString);

			Account acc = accountsRepository.getByEmail(email);
			acc.setPassword(pe.encode(generatedString));
			accountsRepository.save(acc);
			try {
				sendEmail(email, generatedString);
				model.addAttribute("message", "Vui lòng xem email để lấy lại mật khẩu!");
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("messagee", "Đã có lỗi gửi mật khẩu!");
			}
		} else {
			model.addAttribute("messagee", "Mã xác nhận không đúng!");
		}
		return "home/confirmGenerate";
	}

	@GetMapping("/information/{email}")
	public String Information(@PathVariable("email") String email, Model model, Authentication authentication) {
		Account accc = accountsRepository.getByEmail(email);
		model.addAttribute("account", accc);
		String acc = authentication.getName();
		model.addAttribute("acc", acc);
		return "home/information";
	}

	@PostMapping("/information/{accountId}")
	public String InformationSubmit(@PathVariable("accountId") Integer accountId, Model model,
			@ModelAttribute("account") Account account, @RequestParam("image") MultipartFile photo,
			Authentication authentication) {
		Account acccurrent = accountService.getById(accountId);
		String acc = authentication.getName();
		model.addAttribute("acc", acc);
		if (photo != null && !photo.isEmpty()) {
			MultipartFile[] files = new MultipartFile[] { photo };
			String nameImage = filemanage.ImgSave("CustomerImg", files);
			account.setAvatar(nameImage);
			// try {
			// String fileName = photo.getOriginalFilename();
			// Path fileNameAnPath = Paths.get(UPLOAD_DIRECTORY, fileName);
			// Files.write(fileNameAnPath, photo.getBytes());
			// account.setAvatar(fileName);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		} else {
			String photoname = acccurrent.getAvatar();
			account.setAvatar(photoname);
		}
		if (account.getFullname().isBlank()) {
			model.addAttribute("fullname", "Vui lòng nhập họ và tên!");
			return "home/information";
		}
		if (account.getPhone().isBlank()) {
			model.addAttribute("phone", "Vui lòng nhập số điện thoại!");
			return "home/information";
		}
		if (!account.getPhone().matches("^(0[2|3|5|7|8|9])+([0-9]{8})")) {
			model.addAttribute("phone", "Sai định dạng số điện thoại!");
			return "home/information";
		}
		if (account.getCitizen().isBlank()) {
			model.addAttribute("citizen", "Vui lòng nhập số căn cước công dân!");
			return "home/information";
		}
		if (!account.getCitizen().matches("^\\d{9}|\\d{12}$")) {
			model.addAttribute("citizen", "Sai định dạng số căn cước công dân. Vui lòng nhập 9 hoặc 12 chữ số!");
			return "home/information";
		}
		if (account.getCreateDate() == null) {
			model.addAttribute("date", "Vui lòng chọn ngày tháng năm sinh!");
			return "home/information";
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
				return "home/information";
			}
		}

		String email = acccurrent.getEmail();
		String pass = acccurrent.getPassword();
		System.out.println("pass>> " + pass);
		account.setEmail(email);
		account.setPassword(pass);
		accountsRepository.save(account);
		model.addAttribute("update", "Cập nhật thành công!");

		return "home/information";
	}

	@GetMapping("/logout")
	public String logout() {
		return "forward:/signin";
	}

	private void sendEmail(String email, String password) {
		// gửi email
		mailerService.add(mimeMessage -> {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			try {
				helper.setTo(email);
				helper.setSubject("Nhà Trọ F.E Xin Chào!");
				helper.setText(
						"Bạn đã quên mật khẩu cũ và đã yêu cầu một mật khẩu mới: <br/>Đây là mật khẩu mới của bạn: <span style='font-size: 18px; color: red'>"
								+ password + "</span>.",
						true);

			} catch (MessagingException e) {
				e.printStackTrace();
			}
		});
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

	@GetMapping("/payment/{email}")
	public String payment(@PathVariable("email") String email, Model model) {
		List<Invoice> invoices = invoiceRepository.findByAccountEmail(email);
		if (invoices == null) {
			model.addAttribute("hideForm", true);
		} else {
			model.addAttribute("hideForm", false);
			model.addAttribute("invoicess", invoices);
		}
		return "/home/payment_invoice";
	}

	@GetMapping("/history-invoice/{email}")
	public String historyInvoice(@PathVariable("email") String email, Model model) {
		List<Invoice> invoices = invoiceRepository.findByAccountEmailStatus(email);

		model.addAttribute("invoices", invoices);

		return "/home/history_invoice";
	}

	@GetMapping("/payment_infor")
	public String transaction(
			@RequestParam(value = "vnp_OrderInfo") String orderInfo,
			@RequestParam(value = "vnp_ResponseCode") String responseCode, Model model) {
		String invoiceIdString = orderInfo.substring(orderInfo.length() - 2);
		System.out.println(invoiceIdString);

		if (responseCode.equals("00")) {
			Invoice invoice = invoiceRepository.findById(Integer.parseInt(invoiceIdString))
					.orElseThrow(() -> new IllegalArgumentException("Không tồn tại hóa đơn này"));
			invoice.setInvoiceStatus(invoiceStatusRepository.findAll().get(0));
			invoiceRepository.save(invoice);

			model.addAttribute("paymentSuccess", true);
		}

		return "/home/payment_invoice";
	}

	private String randompassword() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
