package com.example.hrautomationbackend.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.hrautomationbackend.entity.ProductEntity;
import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.ProductNotFoundException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.repository.ProductRepository;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class S3Service {

    private final String bucketName;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ProductService productService;

    public S3Service(UserRepository userRepository, ProductRepository productRepository, UserService userService, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.bucketName = "hr-automation";
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public static AmazonS3 buildS3() {
        AwsSdkMetrics.disableMetrics();
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "us-west-2"
                        )
                )
                .build();
    }

    public S3Object uploadPicture(File file) {
        AmazonS3 s3 = buildS3();
        String key = file.getName();
        try {
            PutObjectRequest photo = new PutObjectRequest(bucketName, key, file);
            s3.putObject(photo);
            return s3.getObject(new GetObjectRequest(bucketName, photo.getKey()));
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }

    public void uploadUserPicture(File file, Long id) throws UserNotFoundException {

        S3Object object = uploadPicture(file);
        UserEntity userEntity = userService.getUserEntity(id);
        userEntity.setPictureUrl(String.valueOf(object.getObjectContent().getHttpRequest().getURI()));
        userRepository.save(userEntity);
    }


    public void uploadProductPicture(File file, Long id) throws ProductNotFoundException {
        S3Object object = uploadPicture(file);
        ProductEntity productEntity = productService.getProduct(id);
        productEntity.setPictureUrl(String.valueOf(object.getObjectContent().getHttpRequest().getURI()));
        productRepository.save(productEntity);
    }


//    public URI downloadPicture(Long id) throws UserNotFoundException {
//        AmazonS3 s3 = buildS3();
//        UserEntity userEntity = userService.getUserEntity(id);
//        String photo = userEntity.getPictureUrl();
//        S3Object object = s3.getObject(new GetObjectRequest(bucketName, photo));
//        return object.getObjectContent().getHttpRequest().getURI();
//    }

    /*
     * Delete an object - Unless versioning has been turned on for your bucket,
     * there is no way to undelete an object, so use caution when deleting objects.
     */
//            System.out.println("Deleting an object\n");
//            s3.deleteObject(bucketName, key);

    public File createSampleFile(MultipartFile multipartFile) throws IOException {
        String prefix = multipartFile.getName() + "-hr-automation-";
        File file = File.createTempFile(prefix, ".jpg");
        file.deleteOnExit();
        return file;
    }

}
