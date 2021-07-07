package com.example.repository;

import com.example.pojo.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMongoDbRepository extends MongoRepository<User, String> {

}