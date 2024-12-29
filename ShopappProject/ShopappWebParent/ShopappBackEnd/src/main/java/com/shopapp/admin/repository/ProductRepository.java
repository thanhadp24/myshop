package com.shopapp.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	public Product findByName(String name);
	
	public Long countById(Integer id);
	
	@Query("UPDATE Product p SET p.enabled = :enabled WHERE p.id = :id")
	@Modifying
	public void updateEnabled(Integer id, boolean enabled);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% "
			+ " OR p.shortDescription LIKE %:keyword% "
			+ " OR p.fullDescription LIKE %:keyword% "
			+ " OR p.brand.name LIKE %:keyword% "
			+ " OR p.category.name LIKE %:keyword% ")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.category.id = :categoryId "
			+ " OR p.category.allParentIds LIKE %:categoryIdMatch%")
	public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (p.category.id = :categoryId "
			+ " OR p.category.allParentIds LIKE %:categoryIdMatch%) AND "
			+ " (p.name LIKE %:keyword% "
			+ "	OR p.shortDescription LIKE %:keyword% "
			+ "	OR p.fullDescription LIKE %:keyword% "
			+ "	OR p.brand.name LIKE %:keyword% "
			+ "	OR p.category.name LIKE %:keyword%) ")
	public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, 
			String keyword, Pageable pageable);
}
