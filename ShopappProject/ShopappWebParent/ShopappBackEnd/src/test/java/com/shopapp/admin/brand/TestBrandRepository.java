package com.shopapp.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.BrandRepository;
import com.shopapp.common.entity.Brand;
import com.shopapp.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestBrandRepository {

	@Autowired
	private BrandRepository repository;
	
	@Test
	public void testCreateBrand() {
		Brand brand = new Brand("Apple");
		Set<Category> categories = Set.of(new Category(4), new Category(7));
		brand.setCategories(categories);
		Brand savedBrand = repository.save(brand);
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetBrands() {
		repository.findAll().forEach(System.out::println);
	}
	
	@Test 
	public void testGetById() {
		Brand brand =  repository.findById(1).get();
		System.out.println(brand);
	}
	@Test 
	public void testUpdate() {
		Brand brand =  repository.findById(1).get();
		brand.setName("Acer1");
		Brand savedBrand = repository.save(brand);
		
		System.out.println(savedBrand);
	}
	
}
