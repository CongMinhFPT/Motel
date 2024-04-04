package com.motel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.motel.entity.Account;
import com.motel.entity.FavoriteRoom;
import com.motel.entity.MotelRoom;

public interface AccountsRepository extends JpaRepository<Account, Integer>{

	Account getByEmail(String email);

	
	Account findByPhone(String phone);
	
	Account getByCitizen(String citizen);


	@Query(nativeQuery = true, value = "SELECT * FROM accounts WHERE phone LIKE CONCAT('%', :phone, '%')")
    List<Account> getByPhone(@Param("phone") String phone);

	@Query(nativeQuery = true, value = "select * from accounts a join renter b on a.account_id=b.account_id")
    List<Account> getRenterByRenters();

}
