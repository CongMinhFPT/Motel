package com.motel.entity;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
	
	@NotBlank(message =  "Vui lòng nhập mật khẩu hiện tại!")
	String oldPassword;
	
	@NotBlank(message = "Vui lòng nhập mật khẩu mới!")
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự!")
	String newPassword;
	
	@NotBlank(message = "Vui lòng xác nhận mật khẩu!")
	@Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự!")
	@Transient
	String confirmPassword;
}
