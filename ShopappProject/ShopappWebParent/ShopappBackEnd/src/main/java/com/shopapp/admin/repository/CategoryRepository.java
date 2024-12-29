package com.shopapp.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public List<Category> getRootCategories(Sort sort); // use form select
	
	@Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
	public Page<Category> getRootCategories(Pageable pageable); // use in list category
		

	@Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
	public Page<Category> search(String keyword, Pageable pageable);
	
	public Category findByName(String name);
	
	public Category findByAlias(String alias);
	
	public Long countById(Integer id);
	
	@Query("UPDATE Category c SET c.enabled = :enabled WHERE c.id = :id")
	@Modifying
	public void updateEnabled(Integer id, boolean enabled);
	
}
