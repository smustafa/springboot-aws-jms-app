package com.example;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.example.repository.UserMongoDbRepository;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class AppTestConfiguration {

    @Value("${com.example.userPDFsBucket}")
    private String userPDFsBucket;

    private static final GenericContainer activeMQContainer;
    private static final LocalStackContainer localStackContainer;
    private static final MongoDBContainer mongoDBContainer;

    static {
        activeMQContainer = new GenericContainer<>("rmohr/activemq:latest")
                .withExposedPorts(61616);

        localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.14"))
                .withServices(LocalStackContainer.Service.S3)
                .withEnv("DEFAULT_REGION", "us-east-1");

        mongoDBContainer = new MongoDBContainer("mongo:4.4.2");


        activeMQContainer.start();
        localStackContainer.start();
        mongoDBContainer.start();

    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        System.out.println("NOOOOOOOOOOOOOOOOOOOOOO");
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
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


    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        //ReplicaSetUrl will look similar to mongodb://localhost:33557/test
        ConnectionString connectionString = new ConnectionString(mongoDBContainer.getReplicaSetUrl());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        MongoClient mongoClient = MongoClients.create(mongoClientSettings);

        return new MongoTemplate(mongoClient, "test");
    }


}

