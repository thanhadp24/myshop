package com.shopapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.repository.CategoryRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestCategory {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Test
	public void testGetByAlias() {
		var category = categoryRepository.findByAlias("camera_lenses");
		System.out.println(category);
	}
	
}
