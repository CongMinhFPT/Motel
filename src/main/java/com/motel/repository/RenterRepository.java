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
import com.motel.entity.Renter;

public interface RenterRepository extends JpaRepository<Renter, Integer> {
    @Query("SELECT r FROM Renter r WHERE r.account = :account")
    Renter findByAccount(Account account);
}
