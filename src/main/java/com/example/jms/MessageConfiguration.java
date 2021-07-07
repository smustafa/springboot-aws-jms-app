package com.example.jms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@Configuration
public class MessageConfiguration {


    //Consider setting up a custom DefaultJmsListenerContainerFactory bean.
    //For production purposes, you'll typically fine-tune timeouts, redelivery and recovery settings. Most importantly,
    // the default 'AUTO_ACKNOWLEDGE' mode does not provide reliability guarantees,
    // so make sure to use transacted sessions in case of reliability needs.


    /**
     * This will serialize the User messages into type TextMessage
     */
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

}
