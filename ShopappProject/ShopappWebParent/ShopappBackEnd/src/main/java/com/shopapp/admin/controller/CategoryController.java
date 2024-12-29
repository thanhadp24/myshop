package com.shopapp.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.bean.CategoryPageInfo;
import com.shopapp.admin.common.Common;
import com.shopapp.admin.exporter.CategoryCsvExporter;
import com.shopapp.admin.service.CategoryService;
import com.shopapp.admin.utils.FileUploadUtil;
import com.shopapp.common.entity.Category;
import com.shopapp.common.exception.CategoryNotFoundException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/categories")
	public String viewCategories() {
		return "redirect:/categories/page/1?sortDir=asc&sortField=name";
	}
	
	@GetMapping("/categories/page/{pageNum}")
	public String viewCategoriesPerPage(String sortDir,
			@PathVariable("pageNum") int pageNum, 
			String keyword, Model model) {
		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> categories = categoryService.categoryByPage(pageNum, sortDir, pageInfo, keyword);

		String reverseSortDir = (sortDir.equals("asc") ? "desc" : "asc");
		long startCount = (pageNum - 1) * Common.ROOT_CATEGORY_PER_PAGE + 1;
		long endCount = startCount + Common.ROOT_CATEGORY_PER_PAGE - 1;
		
		if(endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageInfo.getTotalOfPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("categories", categories);
		model.addAttribute("sortField", "name");
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("moduleURL", "/categories");
		
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String categoryForm(Model model) {
		List<Category> categories = categoryService.getCategoryUsedInForm();
		
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categories);
		model.addAttribute("pageTitle", "Create new category");
		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(RedirectAttributes redirectAttributes, 
			Category category, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			
			Category savedCategory = categoryService.save(category);
			
			String path = "../categories-images/" + savedCategory.getId();
			
			FileUploadUtil.cleanDir(path);
			FileUploadUtil.saveFile(path, fileName, multipartFile);
		}else {
			categoryService.save(category);
		}
		
		redirectAttributes.addFlashAttribute("message", "The category name " + category.getName() + " has been saved successfully");
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id,
			Model model, RedirectAttributes ra) {
		
		try {
			model.addAttribute("category", categoryService.get(id));
			model.addAttribute("categories", categoryService.getCategoryUsedInForm());
			model.addAttribute("pageTitle", "Edit category (ID: " + id + ")");
			
			return "categories/category_form";
		} catch (CategoryNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes ra) {
		
		try {
			categoryService.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			ra.addFlashAttribute("message", "Category with id " + id + " has been deleted!");
		} catch (CategoryNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/{id}/enabled/{status}")
	public String enabledCategory(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enable, RedirectAttributes ra) {
		
		categoryService.updateEnabledStatus(id, enable);
		
		String status = enable ? "enabled": "disabled";
		String message = "The user ID " + id + " has been " + status;
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/export/csv")
	public void exportCsv(HttpServletResponse httpServlet) {
		List<Category> categories = categoryService.getCategoryUsedInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		try {
			exporter.exportCsvFile(categories, httpServlet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}