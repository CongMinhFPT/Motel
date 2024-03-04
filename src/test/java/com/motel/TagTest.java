package com.motel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.motel.entity.Tag;
import com.motel.repository.TagRepository;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TagTest {

	
	@Autowired
	private TagRepository tagRepo;
	
	@Test
	public void test1() {
		List<Tag> list = tagRepo.findAll();
		System.out.println(list);
	}
	@Test
	public void test2() {
		Tag tag = new Tag();
		tag.setTitle("tag2");
		tag.setStatus(false);
		Tag save = tagRepo.save(tag);
		assertThat(save.getTagId()).isGreaterThan(0);
	}
}
