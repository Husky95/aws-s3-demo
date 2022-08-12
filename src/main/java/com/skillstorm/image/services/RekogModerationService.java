package com.skillstorm.image.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.rekognition.model.ModerationLabel;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.RekognitionException;

/*
 * AWS Rekognition service for moderating incoming content
 * On integration: replace main with rekognize() as the entry point
 *      - return labels listing?
 * TODO: handle edge-case categories and subcategories
 * TODO: test
 */

@Service
public class RekogModerationService {

  /**
   * Main method is a temporary entry point
   * @param args
   */
  public static void main(String[] args) {
    // test path
    final String path = "G:\\testPictures\\hate-groups-nazi.png";
    
    final String usage = "\n" +
    "Usage: " +
    "  <path>\n\n" +
    "Where:\n" +
    "   path - full path to resource image";
    
//    if (path == null) {
//      System.out.println(usage);
//      System.exit(1);
//    }
    
    rekognize(path);

  }
  
  /**
   * working service method for moderating content
   * will return boolean for abstraction, TRUE for pass, FALSE for fail
   * @param path -image url string
   */
  public static boolean rekognize(String path) {
    
    Region region = Region.US_WEST_1;
    
    RekognitionClient mod = RekognitionClient.builder()
        .region(region)
        .credentialsProvider(ProfileCredentialsProvider.create())
        .build();
    
    try {
      InputStream source = new FileInputStream(path);
      SdkBytes sourceBytes = SdkBytes.fromInputStream(source);
      Image im = Image.builder().bytes(sourceBytes).build();
      
      DetectModerationLabelsRequest modLabelsReq = DetectModerationLabelsRequest.builder()
          .image(im)
          .minConfidence(50F) // will detect labels if it has a confidence level of at least 50%
          // default, can change according to Sean/Patrick/testing
          .build();
      
      DetectModerationLabelsResponse modLabelsResp = mod.detectModerationLabels(modLabelsReq);
      List<ModerationLabel> flags = modLabelsResp.moderationLabels();
        
      
      //debug mode: uncommented
      printLabels(flags);
      
      if (flags.isEmpty()) {
        return true;
      }
    
    } catch (FileNotFoundException | RekognitionException e) {
      e.printStackTrace();
    }
    
    return false;
  }
  
  /**
   * Prints detected moderation labels, output/testing purposes only
   * @param modLabels -list of detected moderation labels
   */
  private static void printLabels(List<ModerationLabel> modLabels) {
    System.out.println("Detected Labels:");
    for (ModerationLabel m : modLabels) {
      System.out.println("Label: " + m.name()
          + "\n Confidence: " + m.confidence()
          + "\n Category: " + m.parentName());
    }
  }

}
