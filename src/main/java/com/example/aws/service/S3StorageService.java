package com.example.aws.service;

import com.amazonaws.services.s3.AmazonS3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class S3StorageService {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${com.example.userPDFsBucket}")
    private String userPDFsBucket;

    public void storeUserPDFDocument(String userId, File fileToStore) {

        System.out.println(amazonS3Client.getRegionName());
        System.out.println(amazonS3Client.getS3AccountOwner());

        System.out.println("BOOOO" + userPDFsBucket + " UserId " +  userId + "fileToStore" + fileToStore);
        amazonS3Client.putObject(userPDFsBucket, userId, fileToStore);

        log.info("Uploaded PDF Document {}, in Bucket: {}", fileToStore.getName(), userPDFsBucket);

    }

    public Boolean confirmFileExistsInS3(String objectName) {
        return amazonS3Client.doesObjectExist(userPDFsBucket, objectName);
    }

}
