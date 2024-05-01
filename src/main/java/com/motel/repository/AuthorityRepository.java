package com.motel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.motel.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer>{

}
