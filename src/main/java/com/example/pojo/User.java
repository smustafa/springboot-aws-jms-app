package com.example.pojo;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * The following outlines what type conversion, if any, is done on the property mapped to the _id document field when
 * using the MappingMongoConverter (the default for MongoTemplate). If possible, an id property or field declared as a
 * String in the Java class is converted to and stored as an ObjectId by using a Spring Converter<String, ObjectId>.
 * Valid conversion rules are delegated to the MongoDB Java driver. If it cannot be converted to an ObjectId, then the
 * value is stored as a string in the database. An id property or field declared as BigInteger in the Java class is
 * converted to and stored as an ObjectId by using a Spring Converter<BigInteger, ObjectId>.
 *
 * Meaning the @Id needs to be of type String or BigInteger.  Will not work with Long
 *
 * https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo-template.id-handling
 */
@Data
@Document("USERS")
public class User {

    //See above for explanation of why String, instead of Long
    @Id
    private String id;

    @NotNull(message = "firstName is not allowed to be null")
    private String firstName;

    @NotNull(message = "lastName is not allowed to be null")
    private String lastName;

    @NotNull(message = "age is not allowed to be null")
    private Integer age;

}
