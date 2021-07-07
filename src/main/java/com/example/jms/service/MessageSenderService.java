package com.example.jms.service;

import com.example.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageSenderService{

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${com.example.userQueue}" )
    private String userQueue;

    public void sendUserMessage(User userMessage){

        log.info("Sending the User: {} to Queue: {}", userMessage, userQueue);

        jmsTemplate.convertAndSend(userQueue, userMessage);
    }
}