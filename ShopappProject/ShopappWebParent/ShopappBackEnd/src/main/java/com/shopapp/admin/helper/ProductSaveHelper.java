package com.shopapp.admin.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopapp.admin.utils.FileUploadUtil;
import com.shopapp.common.entity.Product;
import com.shopapp.common.entity.ProductImage;

public class ProductSaveHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
	
	public static void deleteExtraImgOnForm(Product product) {
		String extraImgDir = "../product-images/" + product.getId() + "/extras/";
		Path dirPath = Paths.get(extraImgDir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				String fileName = file.toFile().getName();
				
				if(!product.containsImageFileName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Delete extra image: " + fileName);
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + fileName);
					}
				}
			});
		} catch (IOException e) {
			LOGGER.error("Could not list directory " + dirPath);
		}
	}

	public static void setExistingExtraImages(String[] imageIds, String[] imageNames, Product product) {
		if(imageIds == null || imageIds.length == 0) return;
		
		Set<ProductImage> images = new HashSet<>();
		
		for(int count = 0; count < imageIds.length; count++) {
			Integer id = Integer.parseInt(imageIds[count]);
			String name = imageNames[count];
			images.add(new ProductImage(id, name, product));
		}
		
		product.setImages(images);
	}
	
	public static void setMainImage(MultipartFile mainImgMultipart, Product product) {
		if(!mainImgMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImgMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
	
	public static void saveUploadedImage(MultipartFile mainImgMultipart, 
			MultipartFile[] extraImgMultiparts, 
			Product savedProduct) throws IOException {
		
		if(!mainImgMultipart.isEmpty()) {
			String dir = "../product-images/" + savedProduct.getId();
			String fileName = StringUtils.cleanPath(mainImgMultipart.getOriginalFilename());
			
			FileUploadUtil.cleanDir(dir);
			FileUploadUtil.saveFile(dir, fileName, mainImgMultipart);	
		}
		
		if(extraImgMultiparts.length > 0) {
			String dir = "../product-images/" + savedProduct.getId() + "/extras";
			for(MultipartFile file: extraImgMultiparts) {
				if(file.isEmpty()) continue;
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				FileUploadUtil.saveFile(dir, fileName, file);	
			}
		}
	}
	
	public static void setNewExtraImage(MultipartFile[] extraImgMultiparts, Product product) {
		if(extraImgMultiparts.length > 0) {
			for(MultipartFile file: extraImgMultiparts) {
				if(!file.isEmpty()) {
					String fileName = StringUtils.cleanPath(file.getOriginalFilename());
					
					if(!product.containsImageFileName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}
	
	public static void setSaveDetails(String[] detailIds, String[] detailNames, 
			String[] detailValues, Product product) {
		if(detailNames == null || detailNames.length == 0) return;
		
		for(int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIds[count]);
			
			if(id != 0) {
				product.addExtraDetail(id, name, value);
			}else if(!name.isEmpty() && !value.isEmpty()) {
				product.addExtraDetail(name, value);
			}
		}
		
	}

}

