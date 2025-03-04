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
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.entity.product.ProductImage;

public class ProductSaveHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
	
//	public static void deleteExtraImgOnForm(Product product) {
//		String extraImgDir = "product-images/" + product.getId() + "/extras";
//		List<String> listKeys = AmazonS3Util.listFolders(extraImgDir);
//		
//		for (String key: listKeys) {
//			int lastIndexOfSlash = key.lastIndexOf("/");
//			String fileName = key.substring(lastIndexOfSlash + 1, key.length());
//			
//			if(!product.containsImageFileName(fileName)) {
//				AmazonS3Util.deleteFile(key);
//			}
//			
//		}
//		
//	}
	
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
			String dir = "product-images/" + savedProduct.getId() + "/extras/";
			String fileName = StringUtils.cleanPath(mainImgMultipart.getOriginalFilename());
			
//			List<String> listKeys = AmazonS3Util.listFolders(dir + "/");
//			for(String key: listKeys) {
//				if(!key.contains("/extras/")) {
//					AmazonS3Util.deleteFile(key);
//				}
//			}
			
			//AmazonS3Util.uploadFile(dir, fileName, mainImgMultipart.getInputStream());
			
			FileUploadUtil.cleanDir(dir);
			FileUploadUtil.saveFile(dir, fileName, mainImgMultipart);	
		}
		
		if(extraImgMultiparts.length > 0) {
			String dir = "product-images/" + savedProduct.getId() + "/extras";
			for(MultipartFile file: extraImgMultiparts) {
				if(file.isEmpty()) continue;
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				
				//AmazonS3Util.uploadFile(dir, fileName, file.getInputStream());
				
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

