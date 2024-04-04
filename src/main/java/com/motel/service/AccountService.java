package com.motel.service;


import java.util.Optional;

import com.motel.entity.Account;

public interface AccountService {
	Optional<Account> findById(Integer id);
	Account getById(Integer id);

}
