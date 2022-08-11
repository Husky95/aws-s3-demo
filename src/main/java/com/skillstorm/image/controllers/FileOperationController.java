package com.skillstorm.image.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.image.services.S3BucketStorageService;

@RestController
public class FileOperationController {

	@Autowired
	private S3BucketStorageService s3BucketStorageService;
	
	@DeleteMapping
	public ResponseEntity<String> deleteFile(@PathVariable String fileName){
		return new ResponseEntity<>(s3BucketStorageService.deleteFileFromS3(fileName), HttpStatus.OK);
	}
	
	
}
