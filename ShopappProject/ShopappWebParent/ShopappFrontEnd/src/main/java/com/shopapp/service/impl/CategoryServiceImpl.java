package com.shopapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopapp.common.entity.Category;
import com.shopapp.common.exception.CategoryNotFoundException;
import com.shopapp.repository.CategoryRepository;
import com.shopapp.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Category> getAllWithNoChildrens() {
		List<Category> categoriesNoChildrens = new ArrayList<>();
		
		List<Category> categoriesEnabled = categoryRepository.findAllEnabled();
		
		categoriesEnabled.forEach(cat -> {
			Set<Category> childrens = cat.getChildren();
			if(childrens == null || childrens.size() == 0) {
				categoriesNoChildrens.add(cat);
			}
		});
		
		return categoriesNoChildrens;
	}
	
	@Override
	public Category getCategory(String alias) throws CategoryNotFoundException {
		try {
			Category category = categoryRepository.findByAlias(alias);
			return category;
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find any category with alias: " + alias);
		} 
	}
	
	@Override
	public List<Category> getParents(Category children) {
		List<Category> parents = new ArrayList<>();
		
		Category parent = children.getParent();
		while(parent != null) {
			parents.add(0, parent);
			parent = parent.getParent();
		}
		
		parents.add(children);
		
		return parents;
	}
}
