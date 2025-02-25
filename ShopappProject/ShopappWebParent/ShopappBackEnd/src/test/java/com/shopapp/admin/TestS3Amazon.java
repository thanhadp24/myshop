package com.shopapp.admin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.shopapp.admin.utils.AmazonS3Util;

public class TestS3Amazon {

	@Test
	public void testListFolder() {
		String folderName = "test-upload";
		AmazonS3Util.listFolders(folderName).forEach(System.out::println);
	}
	
	@Test
	public void testUploadFile() throws FileNotFoundException {
		String folderName = "test-upload/2";
		String fileName = "logo.png";
		String filePath = "C:\\Users\\OS\\Desktop\\" + fileName;
		
		InputStream inputStream = new FileInputStream(filePath);
		AmazonS3Util.uploadFile(folderName, fileName, inputStream);
	}
	
	@Test
	public void testDeleteFile() {
		String fileName = "test-upload/2";
		
		AmazonS3Util.deleteFile(fileName);
	}
	
	@Test
	public void testRemoveFolder() {
		String folderName = "test-upload";
		AmazonS3Util.removeFolder(folderName);
	}
}
