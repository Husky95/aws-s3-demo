package com.skillstorm.image;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.skillstorm.image.services.S3BucketStorageService;

@SpringBootTest
class S3DemoApplicationTests {
	
	private static final Logger log = LoggerFactory.getLogger(S3DemoApplicationTests.class);
	
	@Autowired
	S3BucketStorageService imageStorageService;

	@Test
	void contextLoads() {
	}
	
	// Test upload success, and delete success
	@Test
	void testImageUploadDelete() {
		Path path = Paths.get("src/test/resources/zebra.jpg");
		String name = "zebra.jpg";
		byte[] content = null;
		try {
		    content = Files.readAllBytes(path);
		} catch (final IOException e) {
			e.printStackTrace();
			assertTrue(false, "Unable to read mock image file");
		}
		MultipartFile result = new MockMultipartFile(name, name, "text/plain", content);
		Optional<String> nameOpt = Optional.ofNullable(imageStorageService.uploadFileToS3(result));
		assertTrue(nameOpt.isPresent(), "Name string is null");
		log.info("Bucket image: " + nameOpt.get() + " uploaded");
		assertTrue(nameOpt.get().contains(".jpg"), nameOpt.get() + "did not include the extension");
	}
	// Test attempting to upload a file which is not an image
	// Test uploading an image too large

}
