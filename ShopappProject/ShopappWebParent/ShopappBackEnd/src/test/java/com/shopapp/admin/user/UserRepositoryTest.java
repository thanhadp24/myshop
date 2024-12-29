	package com.shopapp.admin.user;

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
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.UserRepository;
import com.shopapp.common.entity.Role;
import com.shopapp.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testFindUser() {
		String keyword = "bruce";
		Page<User> usersPage = userRepository.findAll(keyword, PageRequest.of(0, 5));
		usersPage.forEach(System.out::println);
	}
	
	@Test
	public void testGetUserByPage() {
		Page<User> users = userRepository.findAll(PageRequest.of(1, 5));
		users.forEach(user -> {
			System.out.println(user);
		});
	}
	
	@Test
	public void createUsers() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		String encodedPass = "$2a$10$75KcP5X6X7xnggL8Js1X1es2YmwL8i.ZHWEtLGafi7h0/yADT3fIC";
		User user = new User("thanhadp@gmail.com", encodedPass, "tran", "thanhdz");
		user.addRole(roleAdmin);
		user.setEnabled(true);
		
		
		userRepository.save(user);
		assertThat(user.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles() {
		User user = new User("thanh@gmail.com", "thanh111", "hello", "thanhdz");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		user.addRole(roleEditor);
		user.addRole(roleAssistant);
		
		User savedUser = userRepository.save(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void findAll() {
		List<User> users = userRepository.findAll();
		users.forEach(System.out::println);
	}
	
	@Test
	public void getById() {
		User user = userRepository.findById(1).get();
		System.out.println(user);
		assertThat(user.getId()).isGreaterThan(0);
	}
	
	@Test
	public void update() {
		User user = userRepository.findById(1).get();
		user.setLastName("coder");
		userRepository.save(user);
		assertThat(user.getId()).isGreaterThan(0);
	}
	
	@Test
	public void updateRole() {
		User user = userRepository.findById(3).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		user.getRoles().remove(roleEditor);
		user.addRole(roleSalesperson);
		
		userRepository.save(user);
		assertThat(user.getId()).isGreaterThan(0);
	}
	
	@Test
	public void delete() {
		userRepository.deleteById(3);
	}
	
	@Test
	public void getByEmail() {
		System.out.println(userRepository.getUserByEmail("thanhadp2402@gmail.com"));
	}
	
	@Test
	public void countById() {
		Long countById = userRepository.countById(1);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
}
