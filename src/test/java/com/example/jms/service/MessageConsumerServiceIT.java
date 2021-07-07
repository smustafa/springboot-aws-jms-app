package com.example.jms.service;

import com.example.AppTestConfiguration;
import com.example.aws.service.S3StorageService;
import com.example.pojo.User;
import com.example.service.UserService;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith(OutputCaptureExtension.class)
@Import(AppTestConfiguration.class)
@SpringBootTest
public class MessageConsumerServiceIT {

    @Autowired
    private MessageSenderService messageSenderService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3StorageService s3StorageService;

    @Test
    void assertMessageConsumedSuccessfully(CapturedOutput logsGenerated) {

        String id = UUID.randomUUID().toString();

        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setId(id);

        Callable<Boolean> userHasBeenAddedToRepo = () -> {
            return userService.getUser(id).isPresent();
        };

        Callable<Boolean> userPDFDocumentAddedToS3 = () -> {
            return s3StorageService.confirmFileExistsInS3(id);
        };

        assertDoesNotThrow(() -> messageSenderService.sendUserMessage(user));

        Awaitility.waitAtMost(5, TimeUnit.SECONDS).until(userHasBeenAddedToRepo);
        Awaitility.waitAtMost(5, TimeUnit.SECONDS).until(userPDFDocumentAddedToS3);
    }
}
