//package com.motel.Until;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.motel.service.BlogService;
//
//
//
//@Controller
//public class UserController {
//
//	@Autowired
//	private BlogService blogService;
////	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@GetMapping("/users")
//	public String listFirstPage() {
//		return "redirect:/users/page/1?sortField=fullName&sortDir=asc";
//	}
//	
//	@GetMapping("/users/page/{pageNum}")
//	public String listByPage(
//			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
//			@PathVariable(name = "pageNum") int pageNum, Model model
//			) {
////		System.out.println("sort field: " + sortField);
////		System.out.println("sort oder: " + sortDir);
//		
//		blogService.listByPage(pageNum, helper);
//		
//		model.addAttribute("pageTitle", "Quản lý nhân viên");
//
//		
//		return "users/users";
//	}
//
//	@GetMapping("/users/newuser")
//	public String newUser(Model model) {
//		List<Role> listRole = service.listRoles();
//
//		User user = new User();
//		user.setEnabled(true);
//
//		model.addAttribute("user", user);
//		model.addAttribute("listRole", listRole);
//		model.addAttribute("pageTitle", "Thêm nhân viên");
//		return "users/newuser_form";
//	}
//
//	@PostMapping("/users/save")
//	public String saveUser(User user, RedirectAttributes redirectAttributes,
////			@RequestParam("currentPass") String currentPass,
//			@RequestParam("image") MultipartFile multipartFile) throws IOException {
//		
//		if(!multipartFile.isEmpty()) {
//			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//			user.setPhotos(fileName);
//			User saveUser = service.save(user);
//			
//			String uploadDir = "user-photos/" + saveUser.getId();
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//			
//		}else {
//			if(user.getPhotos().isEmpty()) user.setPhotos(null);
//			service.save(user);
//			
//		}
////		if(currentPass != null){
////			String oldpass = passwordEncoder.encode(currentPass);
////			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////			String pass = user.getPassword();
////			String paslol = passwordEncoder.encode(user.getPassword());
////			System.out.println("curent input:  " + currentPass);
////			System.out.println("PASSSSSSSS:     " + oldpass);
////			System.out.println("PASSSSSSSS hien tai:     " + pass);
////			System.out.println("authentication hien tai:     " + authentication);
////			System.out.println("lolllllllllll:     " + paslol);
////		}
////		else {
////			currentPass = 
////		}
//		
//		
//		redirectAttributes.addFlashAttribute("message", "Thêm tài khoản thành công");
//		
//		return getRedirectURLtoAffectedUser(user);
//	}
//
//	private String getRedirectURLtoAffectedUser(User user) {
//		String fisrtPartOfEmail = user.getEmail().split("@")[0];
//		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + fisrtPartOfEmail;
//	}
//
//	@GetMapping("/users/edit/{id}")
//	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
//		try {
//			List<Role> listRole = service.listRoles();
//			
//			User user = service.get(id);
//			model.addAttribute("user", user);
//			model.addAttribute("listRole", listRole);
//			model.addAttribute("pageTitle", "Chỉnh sửa thông tin: (" + user.getFullName() + ")");
//
//			return "users/newuser_form";
//		} catch (UserNotFoundException ex) {
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//			return "redirect:/users";
//		}
//	}
//	@GetMapping("/users/delete/{id}")
//	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
//		try {
//			service.delete(id);
//			redirectAttributes.addFlashAttribute("message", "Đã xóa tài khoản (ID: " + id + ")");
//		} catch (UserNotFoundException ex) {
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//			
//		}
//		return "redirect:/users";
//	}
//	@GetMapping("/users/{id}/enabled/{status}")
//	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
//			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
//		service.updateEnabledStatus(id, enabled);
//		String status = enabled ? ": hiện" : ": ẩn";
//		String message = "Tài khoản (ID: "+ id +") đã được cập nhật trạng thái " + status;
//		redirectAttributes.addFlashAttribute("message", message);
//		return "redirect:/users";
//	}
//	
//	@GetMapping("/users/export/csv")
//	public void exportToCSV(HttpServletResponse response) throws IOException {
//		List<User> listUsers = service.listAll();
//		UserCsvExporter exporter = new UserCsvExporter();
//		exporter.export(listUsers, response);
//	}
//	
//	@GetMapping("/users/export/excel")
//	public void exportToExcel(HttpServletResponse response) throws IOException {
//		List<User> listUsers = service.listAll();
//		UserExcelExporter exporter = new UserExcelExporter();
//		exporter.export(listUsers, response);
//	}
//	
//	@GetMapping("/users/export/pdf")
//	public void exportToPDF(HttpServletResponse response) throws IOException {
//		List<User> listUsers = service.listAll();
//		UserPdfExporter exporter = new UserPdfExporter();
//		exporter.export(listUsers, response);
//	}
//
//}
