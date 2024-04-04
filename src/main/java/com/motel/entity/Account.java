package com.motel.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer accountId;
	@NotBlank(message = "Vui lòng nhập họ và tên!")
	@Nationalized
	String fullname;

	// @NotBlank(message = "Vui lòng nhập số điện thoại!")
	// @Size(max = 10, min = 10, message = "Số điện thoài phải 10 số!")
	// @Pattern(regexp = "^(0[2|3|5|7|8|9])+([0-9]{8})", message = "Sai định dạng số
	// điện thoại!")
	String phone;

	@NotBlank(message = "Vui lòng nhập email!")
	@Email(message = "Sai định dạng email!")
	String email;

	// @NotBlank(message = "Vui lòng nhập mật khẩu!")
	// @Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự!")
	String password;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date createDate;

	String avatar;

	boolean gender;

	boolean active = true;
	
//	@Pattern(regexp = "^\\d{9}|\\d{12}$", message = "Số căn cước không hợp lệ. Vui lòng nhập 9 hoặc 12 chữ số.")
     String citizen;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
	List<Authority> authorities;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	List<Post> posts;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
	List<FavoriteRoom> favoriteRoom;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
	List<Blog> blog;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
	List<RequestAuthority> requestAuthorities;


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
    List<Renter> renter;


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonIgnore
	List<Motel> motel;


	@Override
	public String toString() {
		return "Account{" +
				"accountId=" + accountId +
				", fullname='" + fullname + '\'' +
				", phone='" + phone + '\'' +
				", email='" + email + '\'' +
				", createDate=" + createDate +
				", avatar='" + avatar + '\'' +
				", gender=" + gender +
				", active=" + active +
				'}';
	}

}
