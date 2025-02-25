package com.shopapp.admin.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.exception.BrandNotFoundException;
import com.shopapp.admin.exception.BrandRestNotFoundException;
import com.shopapp.admin.service.BrandService;
import com.shopapp.common.dto.CategoryDto;
import com.shopapp.common.entity.Brand;
import com.shopapp.common.entity.Category;

@RestController
public class BrandRestController {

	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(Integer id, String name) {
		return brandService.checkUnique(id, name);
	}
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDto> getCategoriesByBrand(@PathVariable("id") Integer id) throws BrandRestNotFoundException{
		List<CategoryDto> categoryDtos = new ArrayList<>();
		try {
			Brand brand = brandService.get(id);
			Set<Category> categories = brand.getCategories();
			for(Category category: categories) {
				CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());
				categoryDtos.add(categoryDto);
			}
			return categoryDtos;
		} catch (BrandNotFoundException e) {
			throw new BrandRestNotFoundException();
		}
		
	}
}
