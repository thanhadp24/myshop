package com.shopapp.admin.product;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.ProductRepository;
import com.shopapp.common.entity.Brand;
import com.shopapp.common.entity.Category;
import com.shopapp.common.entity.Product;

import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestProductRepository {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void testExtraDetails() {
		Product product = productRepository.findById(2).get();
		
		product.addExtraDetail("country", "VIET NAM");
		product.addExtraDetail("MATERIAL", "VIP");
		
		productRepository.save(product);
	}
	
	@Test
	public void testProductWithImages() {
		Product product = productRepository.findById(2).get();
		
		product.setMainImage("test_image.jpg");
		product.addExtraImage("extra_1.jpg");
		product.addExtraImage("extra_2.jpg");
		product.addExtraImage("extra_3.jpg");
		
		productRepository.save(product);
	}
	
	@Test
	public void testCreate() {
		Brand brand = entityManager.find(Brand.class, 9);
		Category category = entityManager.find(Category.class, 5);
		
		Product product = new Product();
		product.setName("LG XXX");
		product.setAlias("LG XXX");
		product.setShortDescription("A good product");
		product.setFullDescription("LG full details");
		
		product.setPrice(444f);
		product.setCreatedAt(new Date());
		product.setBrand(brand);
		product.setCategory(category);
		
		productRepository.save(product);
	}
	
	@Test
	public void testGetAll() {
		productRepository.findAll().forEach(System.out::println);
	}
}
