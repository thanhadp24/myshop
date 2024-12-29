package com.shopapp.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.RoleRepository;
import com.shopapp.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	
	@Autowired
	private RoleRepository repository;
	
	@Test
	public void testCreateFirstRole() {
		Role role = new Role("Admin", "manage everything");
		Role savedRole = repository.save(role);
		
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateMulRole() {
		Role roleSales = new Role("Salesperson", "manage product price, customers, shipping, orders"
				+ " and sales report");
		Role roleEditor = new Role("Editor", "manage categories, brands, products, "
				+ "articles and menus");
		Role roleShip = new Role("Shipper", "view products, view orders and update order status");
		Role roleAssis = new Role("Assistant", "manage questions and reviews");
		
		repository.saveAll(List.of(roleSales, roleAssis, roleEditor, roleShip));
	}
}
