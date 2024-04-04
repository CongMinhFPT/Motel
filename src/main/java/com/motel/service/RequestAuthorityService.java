package com.motel.service;

import java.util.Optional;

import com.motel.entity.Account;
import com.motel.entity.RequestAuthority;

public interface RequestAuthorityService {

	Optional<RequestAuthority> findById(Integer requestAuthorityId);
	
}
