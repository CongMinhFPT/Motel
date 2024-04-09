package com.motel.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.repository.AccountsRepository;
import com.motel.service.AccountService;

@Service
public class AccountsImpl implements AccountService{

	@Autowired
	AccountsRepository accountsRepository;

	@Override
	public Optional<Account> findById(Integer id) {
		return accountsRepository.findById(id);
	}

	@Override
	public Account getById(Integer id) {
		return accountsRepository.findById(id).get();
	}



}
