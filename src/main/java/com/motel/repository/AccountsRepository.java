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

public interface AccountsRepository extends JpaRepository<Account, Integer> {
	Account getByEmail(String email);

	Account findByPhone(String phone);

	Account getByCitizen(String citizen);

	@Query(nativeQuery = true, value = "SELECT * FROM accounts a INNER JOIN authorities b ON a.account_id = b.account_id WHERE b.role_id = 'CUSTOMER' AND a.citizen LIKE CONCAT('%', :phone, '%') AND NOT EXISTS ( SELECT 1 FROM renter c WHERE c.account_id = a.account_id )")
	List<Account> getByPhone(@Param("phone") String phone);

	@Query(nativeQuery = true, value = "select * from accounts a join renter b on a.account_id=b.account_id")
	List<Account> getRenterByRenters();

	@Query(nativeQuery = true, value = "SELECT * FROM accounts WHERE email LIKE CONCAT('%', :find, '%')")
	List<Account> findByEmail(@Param("find") String find);

	@Query(nativeQuery = true, value = "SELECT * FROM accounts WHERE phone LIKE CONCAT('%', :find, '%')")
	List<Account> findByPhone1(@Param("find") String find);

	@Query(nativeQuery = true, value = "SELECT * FROM accounts WHERE citizen LIKE CONCAT('%', :find, '%')")
	List<Account> findByCitizen(@Param("find") String find);

	@Query(nativeQuery = true, value = "select * from accounts a inner join motels b on a.account_id = b.account_id inner join authorities c on a.account_id = c.account_id where c.role_id = 'CUSTOMER' and a.account_id =:accountId")
	List<Account> findAccountsMotel(@Param("accountId") Integer accountId);
}
