package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.motel.entity.RequestAuthorityStatus;

public interface RequestAuthorityStatusRepository extends JpaRepository<RequestAuthorityStatus, Integer>{

	@Query(nativeQuery = true, value = "select * from request_authority_status where request_authority_status_id = '2' or request_authority_status_id = '3'")
	List<RequestAuthorityStatus> findAllStatus();
}
