package com.example.jms.service;

import com.example.aws.service.S3StorageService;
import com.example.pojo.User;
import com.example.service.GeneratePDFService;
import com.example.service.UserService;
import com.itextpdf.text.DocumentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageConsumerService {

    @Autowired
    private UserService userService;

    @Autowired
    private GeneratePDFService generatePDFService;

    @Autowired
    private S3StorageService s3StorageService;

    @JmsListener(destination = "${com.example.userQueue}")
    public void receiveMessage(User userMessage) throws DocumentException, IOException {

        System.out.println("AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        userService.insert(userMessage);

        //Create PDF and store into S3
        File pdfFile = generatePDFService.generateUserInformationPDFFile(userMessage);
        s3StorageService.storeUserPDFDocument(userMessage.getId(), pdfFile);

    }

}
