package com.skillstorm.image.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3BucketStorageService {

	String uploadFileToS3(MultipartFile file);
	
	byte[] downloadFileFromS3(String fileName);
	
	String deleteFileFromS3(String fileName);
	
}
