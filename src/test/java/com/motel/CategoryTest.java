package com.motel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.motel.entity.CategoryRoom;
import com.motel.repository.CategoryRoomRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryTest {

	@Autowired
	private CategoryRoomRepository repo;
	
	@Test
	public void testCreateBrand1() {
		CategoryRoom c = new CategoryRoom();
		
		c.setTitle("ffff");
		c.setStatus(true);
		
		CategoryRoom savedCate = repo.save(c);
		
		assertThat(savedCate).isNotNull();
		assertThat(savedCate.getCategoryRoomId()).isGreaterThanOrEqualTo(0);
		
	}
}
