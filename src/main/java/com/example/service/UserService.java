package com.example.service;

import com.example.pojo.User;
import com.example.repository.UserMongoDbRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMongoDbRepository userMongoDbRepository;

    public void insert(User userMessage) {
        //Store into Collection
        User insertedUser = userMongoDbRepository.insert(userMessage);
        log.info("ID for Inserted User: {}", insertedUser.getId());

    }

    public Optional<User> getUser(String id) {
        return userMongoDbRepository.findById(id);
    }

    public List<User> findAll() {
        return userMongoDbRepository.findAll();
    }

    public void deleteAll() {
        userMongoDbRepository.deleteAll();
    }
}
