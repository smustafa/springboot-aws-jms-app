package com.example;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class AppTestConfiguration {

    @Value("${com.example.userPDFsBucket}")
    private String userPDFsBucket;

    private static final GenericContainer activeMQContainer;
    private static final LocalStackContainer localStackContainer;

    static {
        activeMQContainer = new GenericContainer<>("rmohr/activemq:latest")
                .withExposedPorts(61616);

        localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.14"))
                .withServices(LocalStackContainer.Service.S3)
                .withEnv("DEFAULT_REGION", "us-east-1");


        activeMQContainer.start();
        localStackContainer.start();

    }

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localStackContainer.getEndpointConfiguration(LocalStackContainer.Service.S3))
                .withCredentials(localStackContainer.getDefaultCredentialsProvider())
                .build();

            if (!amazonS3.doesBucketExistV2(userPDFsBucket)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
                amazonS3.createBucket(new CreateBucketRequest(userPDFsBucket));
            }

        return amazonS3;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("vm://localhost:" + activeMQContainer.getMappedPort(61616));

        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory);
        jmsTemplate.setMessageConverter(converter);

        return jmsTemplate;
    }

}

