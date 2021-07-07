package com.example.jms.service;

import com.example.AppTestConfiguration;
import com.example.pojo.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@Import(AppTestConfiguration.class)
@SpringBootTest
public class MessageSenderServiceIT {

    @Autowired
    private MessageSenderService messageSenderService;

    @Test
    void assertMessageSentSuccessfully(CapturedOutput logsGenerated){

        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setAge(38);

        Assertions.assertDoesNotThrow(() -> messageSenderService.sendUserMessage(user));

        assertThat(logsGenerated.getOut()).contains("Sending the User: User(id=null, firstName=FirstName, lastName=LastName, age=38) to Queue: user.queue");
    }



}
