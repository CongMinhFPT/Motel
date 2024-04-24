package com.motel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Account;
import com.motel.entity.RequestAuthority;

public interface RequestAuthorityRepository extends JpaRepository<RequestAuthority, Integer>{

	@Query(nativeQuery = true, value = "select * from request_authority re inner join request_authority_status req on re.request_authority_status_id = req.request_authority_status_id \r\n"
			+ "inner join accounts a on re.account_id = a.account_id where a.email = :email")
	List<RequestAuthority> findRequest(@Param("email") String account);

	@Query(nativeQuery = true, value = "select * from request_authority where request_authority_status_id = '1' or request_authority_status_id = '2' or request_authority_status_id = '3'")
	List<RequestAuthority> findRequestadmin();
	
	@Query(nativeQuery = true, value = "select COUNT(*) from request_authority where request_authority_status_id = '1'")
	Integer findRequestCount();
	Optional<RequestAuthority> findById(Integer requestAuthorityId);
	
	 boolean existsByRequestAuthorityStatusRequestAuthorityStatusIdAndAccountAccountId(Integer requestAuthorityStatusId, Integer accountId);
	 
	 @Query(nativeQuery = true, value = "select * from request_authority where request_authority_status_id = 1 AND account_id = :accountId")
	 List<RequestAuthority> findByRequestAuthorityId(@Param("accountId") Integer requestAuthorityId);
	 

}
