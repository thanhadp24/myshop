package com.shopapp.admin.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopapp.admin.security.ShopappUserDetails;
import com.shopapp.admin.service.UserService;
import com.shopapp.admin.utils.FileUploadUtil;
import com.shopapp.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopappUserDetails loggedUser,
			Model model) {
		String email = loggedUser.getUsername();
		User user = userService.getUserByEmail(email);
		
		model.addAttribute("user", user);
		
		return "users/account_form";
	}
	
	@PostMapping("/account/update")
	public String updateDetails(User user, RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal ShopappUserDetails loggedUser,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			user.setPhotos(fileName);
			User savedUser = userService.updateAccount(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			if (user.getPhotos().isEmpty()) {
				user.setPhotos(null);
			}
			userService.updateAccount(user);
		}
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		redirectAttributes.addFlashAttribute("message", "Your account details has been updated");
		
		return "redirect:/account";
	}
}
