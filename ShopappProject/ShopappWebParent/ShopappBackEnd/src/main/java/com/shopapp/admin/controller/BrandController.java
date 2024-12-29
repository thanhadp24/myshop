package com.shopapp.admin.controller;

import java.io.IOException;

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

import com.shopapp.admin.exception.BrandNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.paging.PagingAndSortingParam;
import com.shopapp.admin.service.BrandService;
import com.shopapp.admin.service.CategoryService;
import com.shopapp.admin.utils.FileUploadUtil;
import com.shopapp.common.entity.Brand;

@Controller
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String viewBrands() {
		return "redirect:/brands/page/1?sortDir=asc&sortField=name";
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String viewBrandPerPage(
			@PagingAndSortingParam(listName = "brands", moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum, 
			String keyword, Model model) {
		System.out.println("keyword " + keyword);
		brandService.getByPage(pageNum, helper);
		
		return "brands/brands";
	}
	
	@GetMapping("/brands/new")
	public String createBrand(Model model) {
		model.addAttribute("brand", new Brand());
		model.addAttribute("categories", categoryService.getCategoryUsedInForm());
		model.addAttribute("pageTitle", "Create Brand");
		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveCategory(RedirectAttributes ra, 
			Brand brand, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand savedBrand = brandService.save(brand);
			String dir = "../brand-logos/" + savedBrand.getId();
			FileUploadUtil.cleanDir(dir);
			FileUploadUtil.saveFile(dir, fileName, multipartFile);
		}else {
			brandService.save(brand);
		}
		
		ra.addFlashAttribute("message", "Brand " + brand.getName() + " has been saved successfully");
		
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Brand brand = brandService.get(id);
			model.addAttribute("brand", brand);
			model.addAttribute("categories", categoryService.getCategoryUsedInForm());
			model.addAttribute("pageTitle", "Edit brand (ID: " + id + ")");
			
			return "brands/brand_form";
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable("id")Integer id, RedirectAttributes ra) {
		try {
			brandService.delete(id);
			String dir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(dir);
			ra.addFlashAttribute("message", "The brand " + id + " has been deleted!");
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/brands";
	}
}
