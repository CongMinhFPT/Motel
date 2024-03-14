package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.motel.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

	@Query(nativeQuery = true, value = "  select * from roles where id LIKE 'MANAGER' OR id LIKE 'STAFF'")
	List<Role> findAllRole();
}
