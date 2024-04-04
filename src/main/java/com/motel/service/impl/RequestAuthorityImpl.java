package com.motel.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.motel.entity.Account;
import com.motel.entity.RequestAuthority;
import com.motel.repository.RequestAuthorityRepository;
import com.motel.service.RequestAuthorityService;

@Service
public class RequestAuthorityImpl implements RequestAuthorityService{
 
	@Autowired
	RequestAuthorityRepository requestAuthorityRepository;
	
	@Override
	public Optional<RequestAuthority> findById(Integer id) {
		return requestAuthorityRepository.findById(id);
	}

}
