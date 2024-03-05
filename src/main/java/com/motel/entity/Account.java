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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

	@NotBlank(message = "Vui lòng nhập số điện thoại!")
	@Size(max = 10, min = 10, message = "Số điện thoài phải 10 số!")
	@Pattern(regexp = "^(0[2|3|5|7|8|9])+([0-9]{8})", message = "Sai định dạng số điện thoại!")
	String phone;

	@NotBlank(message = "Vui lòng nhập email!")
	@Email(message = "Sai định dạng email!")
	String email;

	@NotBlank(message = "Vui lòng nhập mật khẩu!")
	@Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự!")
	String password;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date")
	Date createDate = new Date();

	String avatar;

	boolean gender;

	boolean active = true;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "account")
	@JsonManagedReference
	List<Authority> authorities;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonManagedReference
	List<Post> posts;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonManagedReference
	List<FavoriteRoom> favoriteRoom;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonManagedReference
	List<Blog> blog;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonManagedReference
	List<RequestAuthority> requestAuthorities;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    @JsonManagedReference
    List<Renter> renter;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	@JsonManagedReference
	List<Motel> motel;

	@Override
	public String toString() {
		return "Account{" +
				"id=" + email +
				// ... Các thuộc tính khác không có lời gọi toString()
				'}';
	}
}
