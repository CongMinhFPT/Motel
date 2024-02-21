package com.motel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.motel.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

}
