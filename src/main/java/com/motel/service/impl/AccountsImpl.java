package com.motel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.repository.AccountsRepository;
import com.motel.service.AccountService;

@Service
public class AccountsImpl implements AccountService{

	@Autowired
	AccountsRepository accountsRepository;

}
