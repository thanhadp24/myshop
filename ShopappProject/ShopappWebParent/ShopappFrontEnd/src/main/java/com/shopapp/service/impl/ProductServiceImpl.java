package com.shopapp.service.impl;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopapp.common.Common;
import com.shopapp.common.entity.Product;
import com.shopapp.common.exception.ProductNotFoundException;
import com.shopapp.repository.ProductRepository;
import com.shopapp.service.ProductService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public Page<Product> getByPage(int pageNum, Integer categoryId) {
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum - 1, Common.PRODUCTS_PER_PAGE);
		
		return productRepository.getByPage(categoryId, categoryIdMatch, pageable);
	}
	
	@Override
	public Product getByAlias(String alias) throws ProductNotFoundException {
		try {
			Product product = productRepository.findByAlias(alias);
			return product;
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any product with the alias: " + alias);
		}
	}
	
	@Override
	public Page<Product> search(String keyword, int pageNum) {
		Pageable pageable = PageRequest.of(pageNum - 1, Common.PRODUCTS_PER_PAGE_SEARCH);
		
		return productRepository.search(keyword+"*", pageable); 
	}
}
