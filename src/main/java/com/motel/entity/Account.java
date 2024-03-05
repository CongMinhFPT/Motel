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
import org.springframework.format.annotation.DateTimeFormat;

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

	String phone;

	@NotBlank(message = "Vui lòng nhập email!")
	@Email(message = "Sai định dạng email!")
	String email;

	String password;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date")
	Date createDate = new Date();

	String avatar;

	boolean gender;

	boolean active = true;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "account")
	List<Authority> authorities;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	List<Post> posts;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	List<FavoriteRoom> favoriteRoom;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
	List<Blog> blog;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "serviceId")
	Services services;

	@Override
	public String toString() {
		return "Account{" +
				"id=" + email +
				// ... Các thuộc tính khác không có lời gọi toString()
				'}';
	}
}
