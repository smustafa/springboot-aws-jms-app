package com.example.controller;

import com.example.jms.service.MessageSenderService;
import com.example.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/v1/users")
public class UsersController {

    @Autowired
    private MessageSenderService messageSenderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createUser(@Valid @RequestBody User user) {

        messageSenderService.sendUserMessage(user);

        log.info("Message Sent with User Payload {}", user);
    }
}
