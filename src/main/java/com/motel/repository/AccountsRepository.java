package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.Account;

public interface AccountsRepository extends JpaRepository<Account, Integer>{

	
	Account getByEmail(String email);
}
