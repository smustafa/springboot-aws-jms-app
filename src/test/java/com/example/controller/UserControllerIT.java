package com.example.controller;

import com.example.AppTestConfiguration;
import com.example.aws.service.S3StorageService;
import com.example.pojo.User;
import com.example.service.UserService;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(AppTestConfiguration.class)
@SpringBootTest
public class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private S3StorageService s3StorageService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void test_createUser_validateDBAndS3() throws Exception {

        String id = UUID.randomUUID().toString();

        User userToCreate = new User();
        userToCreate.setId(id);
        userToCreate.setFirstName("Mike");
        userToCreate.setLastName("Fast");
        userToCreate.setAge(50);

        mvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk());

        Callable<Boolean> userHasBeenAddedToRepo = () -> {
            return userService.getUser(id).isPresent();
        };

        Callable<Boolean> userPDFDocumentAddedToS3 = () -> {
            return s3StorageService.confirmFileExistsInS3(id);
        };

        Awaitility.waitAtMost(10, TimeUnit.MINUTES).until(userHasBeenAddedToRepo);
        Awaitility.waitAtMost(10, TimeUnit.SECONDS).until(userPDFDocumentAddedToS3);

    }

    //Lastname set to NULL, which is not valid as it has @NotNull on it
    //We should expect BAD REQUEST with specific error messages
    @Test
    public void test_createUser_with_invalidPayload() throws Exception {

        String id = UUID.randomUUID().toString();

        User userToCreate = new User();
        userToCreate.setId(id);
        userToCreate.setFirstName("FirstName");
        userToCreate.setAge(50);

        MvcResult response = mvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest())
                .andReturn();

        //Ensure validation is triggered
        Assertions.assertEquals("Validation Errors: lastName is not allowed to be null",
                response.getResponse().getContentAsString());
    }

}