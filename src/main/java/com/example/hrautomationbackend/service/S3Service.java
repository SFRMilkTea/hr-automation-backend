package com.example.hrautomationbackend.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.metrics.AwsSdkMetrics;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;

@Service
public class S3Service {

    private final String bucketName;
    private final UserRepository userRepository;
    private final UserService userService;

    public S3Service(UserRepository userRepository, UserService userService) {
        this.bucketName = "hr-automation";
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public static AmazonS3 buildS3() {
        AwsSdkMetrics.disableMetrics();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .withEndpointConfiguration(
                        new AmazonS3ClientBuilder.EndpointConfiguration(
                                "storage.yandexcloud.net", "us-west-2"
                        )
                )
                .build();
        return s3;
    }

    public void uploadPicture(File file, Long id) throws UserNotFoundException {
        AmazonS3 s3 = buildS3();
        String key = file.getName();
        try {
            PutObjectRequest photo = new PutObjectRequest(bucketName, key, file);
            s3.putObject(photo);
            UserEntity userEntity = userRepository
                    .findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не существует"));
            userEntity.setPictureUrl(photo.getKey());
            userRepository.save(userEntity);
        } catch (SdkClientException e) {
            throw new RuntimeException(e);
        }
    }

    public URI downloadPicture(Long id) throws UserNotFoundException {
        AmazonS3 s3 = buildS3();
        UserEntity userEntity = userService.getUserEntity(id);
        String photo = userEntity.getPictureUrl();
        S3Object object = s3.getObject(new GetObjectRequest(bucketName, photo));
        return object.getObjectContent().getHttpRequest().getURI();
    }

    /*
     * Delete an object - Unless versioning has been turned on for your bucket,
     * there is no way to undelete an object, so use caution when deleting objects.
     */
//            System.out.println("Deleting an object\n");
//            s3.deleteObject(bucketName, key);

    public static File createSampleFile(MultipartFile multipartFile) throws IOException {
        String prefix = multipartFile.getOriginalFilename() + "-hr-automation-";
        File file = File.createTempFile(prefix, ".jpg");
        file.deleteOnExit();
        return file;
    }

}
