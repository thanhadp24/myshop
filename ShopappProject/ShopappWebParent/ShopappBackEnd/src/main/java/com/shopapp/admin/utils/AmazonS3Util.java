package com.shopapp.admin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class AmazonS3Util {

	private static final String BUCKET_NAME;
	private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Util.class);
	
	static {
		BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
	}
	
	public static List<String> listFolders(String folderName) {
		S3Client client = S3Client.builder().build();
		
		ListObjectsRequest listRequest = ListObjectsRequest.builder()
					.bucket(BUCKET_NAME).prefix(folderName).build();
		
		ListObjectsResponse response = client.listObjects(listRequest);
		
		List<S3Object> contents = response.contents();
		
		Iterator<S3Object> iterators = contents.iterator();
		
		List<String> keys = new ArrayList<>();
		
		while(iterators.hasNext()) {
			S3Object object = iterators.next();
			keys.add(object.key());
		}
		return keys;
	}
	
	public static void uploadFile(String folderName, String fileName, InputStream inputStream) {
		S3Client client = S3Client.builder().build();
		
		PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET_NAME)
						.key(folderName + "/" + fileName).acl("public-read").build();
		
		try (inputStream){
			int contentLength = inputStream.available();
			client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
		} catch (IOException e) {
			LOGGER.error("Could not load file to amazon S3", e);
		}
	}
	
	public static void deleteFile(String fileName) {
		S3Client client = S3Client.builder().build();
		
		DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME)
						.key(fileName).build();
		client.deleteObject(request);
	}
	
	public static void removeFolder(String folderName) {
		S3Client client = S3Client.builder().build();
		
		ListObjectsRequest listRequest = ListObjectsRequest.builder()
				.bucket(BUCKET_NAME).prefix(folderName).build();
	
	ListObjectsResponse response = client.listObjects(listRequest);
	
	List<S3Object> contents = response.contents();
	
	Iterator<S3Object> iterators = contents.iterator();
	
	
	while(iterators.hasNext()) {
		DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(BUCKET_NAME)
				.key(iterators.next().key()).build();
		client.deleteObject(request);
		}
	}
}
