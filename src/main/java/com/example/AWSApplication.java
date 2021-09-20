package com.example;

import com.example.jms.service.MessageSenderService;
import com.example.pojo.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class AWSApplication {


    public static void main(String[] args){

             // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(AWSApplication.class, args);
    }
}

