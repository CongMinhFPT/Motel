package com.motel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;


import com.motel.repository.AccountsRepository;
import com.motel.service.AccountService;

public class AccountsImpl implements AccountService{

	@Autowired
	AccountsRepository accountsRepository;
	

}
