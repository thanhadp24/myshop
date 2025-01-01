package com.shopapp.admin.controller;

import static com.shopapp.admin.helper.ProductSaveHelper.deleteExtraImgOnForm;
import static com.shopapp.admin.helper.ProductSaveHelper.saveUploadedImage;
import static com.shopapp.admin.helper.ProductSaveHelper.setExistingExtraImages;
import static com.shopapp.admin.helper.ProductSaveHelper.setMainImage;
import static com.shopapp.admin.helper.ProductSaveHelper.setNewExtraImage;
import static com.shopapp.admin.helper.ProductSaveHelper.setSaveDetails;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.paging.PagingAndSortingParam;
import com.shopapp.admin.security.ShopappUserDetails;
import com.shopapp.admin.service.BrandService;
import com.shopapp.admin.service.CategoryService;
import com.shopapp.admin.service.ProductService;
import com.shopapp.admin.utils.FileUploadUtil;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/products")
	public String viewProducts() {
		return "redirect:/products/page/1?sortDir=asc&sortField=name&categoryId=0";
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String viewProductPerPage(
			@PagingAndSortingParam(listName = "products", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum, 
			Integer categoryId, 
			Model model) {
		
		productService.getByPage(pageNum, helper, categoryId);

		if(categoryId != null) {
			model.addAttribute("categoryId", categoryId);
		}

		model.addAttribute("categories", categoryService.getCategoryUsedInForm());

		return "products/products";
	}
	
	
	@GetMapping("/products/new")
	public String createProduct(Model model) {
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("brands", brandService.getAll());
		model.addAttribute("pageTitle", "Create new product");
		model.addAttribute("nboExtraImages", 0);
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, 
			@RequestParam(name = "fileImage", required = false) MultipartFile mainImgMultipart,
			@RequestParam(name = "extraImage", required = false) MultipartFile[] extraImgMultiparts,
			@RequestParam(name = "detailIds", required = false) String[] detailIds,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIds", required = false) String[] imageIds,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal ShopappUserDetails loggedUser,
			RedirectAttributes ra) throws IOException {
		
		if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if(loggedUser.hasRole("Salesperson")) {
				productService.saveProductPrice(product);
				ra.addFlashAttribute("message", "The product has been save successfully");
				return "redirect:/products";
			}
		}
		
		setMainImage(mainImgMultipart, product);
		setExistingExtraImages(imageIds, imageNames, product);
		setNewExtraImage(extraImgMultiparts, product);
		setSaveDetails(detailIds, detailNames, detailValues, product);
		
		Product savedProduct = productService.save(product);
		
		saveUploadedImage(mainImgMultipart, extraImgMultiparts, savedProduct);
		
		deleteExtraImgOnForm(product);
		
		ra.addFlashAttribute("message", "The product has been save successfully");
		
		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String updateProduct(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra, @AuthenticationPrincipal ShopappUserDetails loggedUser) {
		
		try {
			Product product = productService.get(id);
			boolean isReadOnly4Salesperson = false;
			if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if(loggedUser.hasRole("Salesperson")) {
					isReadOnly4Salesperson = true;
				}
			}
			
			model.addAttribute("isReadOnly", isReadOnly4Salesperson);
			model.addAttribute("brands", brandService.getAll());
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Update product(ID:" + id + ")");
			model.addAttribute("nboExtraImages", product.getImages().size());
			
			return "products/product_form";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id,
			RedirectAttributes ra) {
		try {
			productService.delete(id);
			String dir = "../product-images/" + id;
			String extraDir = dir + "/extras";
			
			FileUploadUtil.removeDir(extraDir);
			FileUploadUtil.removeDir(dir);
			
			ra.addFlashAttribute("message", "The product id " + id + " has been deleted!");
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/products";
	}
	
	@GetMapping("/products/detail/{id}")
	public String getProductDetail(@PathVariable("id") Integer id,
			Model model,
			RedirectAttributes ra) {
		
		try {
			Product product = productService.get(id);
			model.addAttribute("product", product);
			return "products/product_detail_modal";
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			
			return "redirect:/products";
		}
		
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String enable(@PathVariable("id") Integer id,
				@PathVariable("status") boolean enable,
				RedirectAttributes ra) {
		
		productService.updateEnabled(id, enable);
		
		String status = enable ? "enabled": "disabled";
		String message = "The product ID " + id + " has been " + status;
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/products";
	}
}
