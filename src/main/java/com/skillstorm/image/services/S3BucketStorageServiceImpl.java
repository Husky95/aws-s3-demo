package com.skillstorm.image.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3BucketStorageServiceImpl implements S3BucketStorageService {

	private static final Logger LOG = LoggerFactory.getLogger(S3BucketStorageServiceImpl.class);
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Value("${application.bucket.name}")
	private String bucketName;
	
	@Value("${features.images.downscaling:true}")
	private boolean downscaling;
	
	@Value("${features.images.max-dimension:450}")
	private int maxDimension;
	
	@Value("${features.images.name-length:32}")
	private int nameLength;
	
	@Override
	public String uploadFileToS3(MultipartFile file) {
		// What about the file name extension???
		File fileObj = convertMultiPartFileToFile(file);
		Optional<String> extOpt = getExtension(file.getOriginalFilename());
		String ext = extOpt.isPresent() ? extOpt.get() : "jpeg";
		// Name collision is astronomically low, but could check anyways
		String fileName = this.generateFileName() + "." + ext;
		if (this.downscaling) {
			BufferedImage img;
			try {
				img = ImageIO.read(fileObj);
				img = downscaleImage(img);
				ImageIO.write(img, ext, fileObj);
			} catch (IOException io) {
				LOG.error("Exception occured while downscaling image", io);
				return null;
			}
		}
		PutObjectResult por = this.amazonS3.putObject(this.bucketName, fileName, fileObj);
		fileObj.delete();
		if (Objects.nonNull(por)) {
			LOG.trace(String.format("File %s successfully uploaded to %s", fileName, this.bucketName));
			return fileName;
		}
		LOG.error(String.format("File %s failed to upload to %s", file.getOriginalFilename(), this.bucketName));
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
	
	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException io) {
			LOG.error("Exception occured while converting file", io);
		}
		return convertedFile;
	}
	
	private String generateFileName() {
		String generatedString = RandomStringUtils.randomAlphanumeric(this.nameLength);
		LOG.trace("Generated name: " + generatedString);
		return generatedString;
	}
	
	private BufferedImage downscaleImage(BufferedImage img) {
		return Scalr.resize(img, this.maxDimension);
	}
	
	public Optional<String> getExtension(String filename) {
	    return Optional.ofNullable(filename)
	      .filter(f -> f.contains("."))
	      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

}
