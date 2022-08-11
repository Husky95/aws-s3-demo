package com.skillstorm.image.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.image.services.S3BucketStorageService;

@RestController
public class FileOperationController {

	@Autowired
	private S3BucketStorageService s3BucketStorageService;
	
}
