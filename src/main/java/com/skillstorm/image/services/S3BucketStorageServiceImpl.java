package com.skillstorm.image.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

public class S3BucketStorageServiceImpl implements S3BucketStorageService {

	private static final Logger LOG = LoggerFactory.getLogger(S3BucketStorageServiceImpl.class);
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Override
	public String uploadFileToS3(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] downloadFileFromS3(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteFileFromS3(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

}
