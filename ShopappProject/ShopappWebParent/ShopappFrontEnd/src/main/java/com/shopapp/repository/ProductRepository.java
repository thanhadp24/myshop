package com.shopapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = :categoryId OR p.category.allParentIds LIKE %:categoryIdMatch%) "
			+ "ORDER BY p.name ASC")
	public Page<Product> getByPage(Integer categoryId, String categoryIdMatch, Pageable pageable);
	
	public Product findByAlias(String alias);
	
	@Query(value = "SELECT * FROM products WHERE enabled = true AND "
			+ " MATCH(name, short_description, full_description) "
			+ " AGAINST(:keyword IN BOOLEAN MODE)", nativeQuery = true)
	public Page<Product> search(String keyword, Pageable pageable);
}
