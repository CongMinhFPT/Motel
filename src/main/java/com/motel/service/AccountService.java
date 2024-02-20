package com.motel.service;

import com.motel.entity.Account;

public interface AccountService {

	Account getByEmail(String email);
}
