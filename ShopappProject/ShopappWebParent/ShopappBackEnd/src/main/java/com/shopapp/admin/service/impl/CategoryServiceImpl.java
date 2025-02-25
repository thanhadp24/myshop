package com.shopapp.admin.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopapp.admin.bean.CategoryPageInfo;
import com.shopapp.admin.common.Common;
import com.shopapp.admin.repository.CategoryRepository;
import com.shopapp.admin.service.CategoryService;
import com.shopapp.common.entity.Category;
import com.shopapp.common.exception.CategoryNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Category> categoryByPage(int pageNum, String sortDir, 
			CategoryPageInfo categoryPageInfo, String keyword) {
		Sort sort = Sort.by("name").ascending();
			
		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")){
			sort = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum - 1, Common.ROOT_CATEGORY_PER_PAGE, sort);
		Page<Category> pageCategories = null;
		if(keyword != null && !keyword.isEmpty()) {
			pageCategories = categoryRepository.search(keyword, pageable);
		}else {
			pageCategories = categoryRepository.getRootCategories(pageable);
		}
		
		List<Category> rootCategories = pageCategories.getContent();
		
		categoryPageInfo.setTotalOfPages(pageCategories.getTotalPages());
		categoryPageInfo.setTotalElements(pageCategories.getTotalElements());
		
		if(keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			for(Category res: searchResult) {
				res.setHasChildren(res.getChildren().size() > 0);
			}
			return searchResult;
		}else {
			return listHierarchicalCategories(rootCategories, sortDir);
		}
	}
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir){
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for (Category category: rootCategories) {
			hierarchicalCategories.add(Category.copyFull(category)); // avoid update db
			
			listSubCategoriesHierarchical(hierarchicalCategories, category, 0, sortDir);
		}
		
		return hierarchicalCategories;
	}
	
	private void listSubCategoriesHierarchical(List<Category> hierarchicalCategories,
			Category parent, int subLevel, String sortDir) {
		int newSubLevel = subLevel + 1;
		for (Category subCategory: sortSubCategories(parent.getChildren(), sortDir)) {
			StringBuilder sb = new StringBuilder("");
			for(int i = 0; i < newSubLevel; i++) {
				sb.append("--");
			}
			String name = sb.append(" ").append(subCategory.getName()).toString();
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			listSubCategoriesHierarchical(hierarchicalCategories, subCategory, newSubLevel, sortDir);
		}
	}
	
	@Override
	public List<Category> getCategoryUsedInForm() {
		List<Category> categoriesInForm = new ArrayList<>();
		List<Category> categories = categoryRepository.findAll(Sort.by("name").ascending());
		
		for (Category category: categories) {
			if(category.getParent() == null) {
				categoriesInForm.add(Category.copyIdAndName(category));
				listSubCategoriesUsedInForm(category, 0, categoriesInForm);
			}
		}
		
		return categoriesInForm;
	}
	
	@Override
	public Category save(Category category) {
		Category parent = category.getParent();
		if(parent != null) {
			String allParentIds = parent.getAllParentIds() == null ? "-" : parent.getAllParentIds();
			allParentIds += String.valueOf(parent.getId()) + "-";
			category.setAllParentIds(allParentIds);
		}
		
		if(category.getAlias().isEmpty() || category.getAlias() == null) {
			category.setAlias(category.getName().replaceAll(" ", "-"));
		}else {
			category.setAlias(category.getAlias().replaceAll(" ", "-"));
		}
		
		return categoryRepository.save(category);
	}
	
	private void listSubCategoriesUsedInForm(Category parent, int subLevel, List<Category> categoriesInForm) {
		int newSubLevel = subLevel + 1;
		for (Category subCategory: sortSubCategories(parent.getChildren())) {
			StringBuilder sb = new StringBuilder("");
			for(int i = 0; i < newSubLevel; i++) {
				sb.append("--");
			}
			String name = sb.append(" ").append(subCategory.getName()).toString();
			categoriesInForm.add(Category.copyIdAndName(subCategory.getId(), name));
			listSubCategoriesUsedInForm(subCategory, newSubLevel, categoriesInForm);
		}
	}
	
	@Override
	public Category get(int id) throws CategoryNotFoundException {
		try {
			return categoryRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new CategoryNotFoundException("Could not find category with id " + id);
		}
	}
	
	@Override
	public void delete(int id) throws CategoryNotFoundException {
		Long count = categoryRepository.countById(id);
		if(count == null || count == 0) {
			throw new CategoryNotFoundException("Could not find any category with id " + id);
		}
		categoryRepository.deleteById(id);
	}
	
	@Override
	public String checkUnique(Integer id, String name, String alias) {
		
		boolean isCreatingNew = (id == null || id == 0);
		Category categoryByName = categoryRepository.findByName(name);
		Category categoryByAlias = categoryRepository.findByAlias(alias);
		
		if(isCreatingNew) {
			if(categoryByName != null) {
				return "Duplicate name";
			}else if(categoryByAlias != null) {
				return "Duplicate alias";
			}
		}else {
			if(categoryByName != null && categoryByName.getId() != id) {
				return "Duplicate name";
			}else if(categoryByAlias != null && categoryByAlias.getId() != id) {
				return "Duplicate alias";
			}
		}
		
		return "OK";
	}
	
	@Override
	public void updateEnabledStatus(Integer id, boolean enable) {
		categoryRepository.updateEnabled(id, enable);
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> categories){
		return sortSubCategories(categories, "asc");
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> categories, String sortDir){
		SortedSet<Category> sortedSubCategories = new TreeSet<>(new Comparator<Category>() {
			@Override
			public int compare(Category c1, Category c2) {
				if(sortDir.equals("asc")) {
					return c1.getName().compareTo(c2.getName());
				}else {
					return c2.getName().compareTo(c1.getName());
				}
			}
		});
		sortedSubCategories.addAll(categories);
		return sortedSubCategories;
	}
}
