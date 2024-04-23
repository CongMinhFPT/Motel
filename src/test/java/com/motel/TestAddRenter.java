package com.motel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.motel.entity.Account;
import com.motel.entity.MotelRoom;
import com.motel.entity.Renter;
import com.motel.repository.AccountsRepository;
import com.motel.repository.MotelRoomRepository;
import com.motel.repository.RenterRepository;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestAddRenter {
	
	@Autowired
	private RenterRepository rdao;
	
	@Autowired
	private AccountsRepository adao;
	
	@Autowired
	private MotelRoomRepository mdao;
	
	@Test
	public void testAd() {
		
		Account account = adao.findById(1).get();
		MotelRoom motelRoom = mdao.findById(1).get();
		
		Renter renter = new Renter();
		renter.setAccount(account);
		renter.setMotelRoom(motelRoom);
		renter.setRenterDate(new Date());
		
		rdao.save(renter);
		
	}
	

}
