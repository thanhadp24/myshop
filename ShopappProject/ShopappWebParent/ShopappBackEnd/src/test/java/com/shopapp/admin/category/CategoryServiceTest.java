package com.shopapp.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopapp.admin.repository.CategoryRepository;
import com.shopapp.admin.service.impl.CategoryServiceImpl;
import com.shopapp.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

	@MockBean
	private CategoryRepository repo;
	
	@InjectMocks
	private CategoryServiceImpl service;
	
	@Test
	public void test() {
		Integer id = null;
		String name = "Computers";
		String alias = "abc";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(category);
		String res = service.checkUnique(id, name, alias);
		
		assertThat(res).isEqualTo("Duplicate Name");
	}
	
	@Test
	public void testExist() {
		Integer id = 1;
		String name = "Computers";
		String alias = "abc";
		
		Category category = new Category(2, name, alias);
		
		Mockito.when(repo.findByName(name)).thenReturn(category);
		Mockito.when(repo.findByAlias(alias)).thenReturn(null);
		
		String res = service.checkUnique(id, name, alias);
		
		assertThat(res).isEqualTo("Duplicate name");
	}
}
