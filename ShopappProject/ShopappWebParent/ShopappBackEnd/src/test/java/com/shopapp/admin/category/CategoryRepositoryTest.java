package com.shopapp.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.admin.repository.CategoryRepository;
import com.shopapp.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testCategory() {
		Category category = repo.findByName("Computers");
		System.out.println(category);
	}
	
	
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Computers");
		Category savedCategory = repo.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(1);
		Category laptops = new Category("televisions", parent);
		Category desktops = new Category("smartphones", parent);
		
		repo.saveAll(List.of(laptops, desktops));
	}
	
	@Test
	public void testGetSubCategory() {
		Category category = repo.findById(2).get();
		System.out.println(category);
	}
	
	
	@Test
	public void testGetHierachicalCategory() {
		var categories = repo.findAll();
		
		for (Category category: categories) {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				printChildren(category, 0);
			}
		}
			
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		for (Category subCategory: parent.getChildren()) {
			for(int i = 0; i < newSubLevel; i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
			printChildren(subCategory, newSubLevel);
		}
	}
}
