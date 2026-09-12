package com.user_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${Kafka.topics.userTopic}")
    private String userTopic;

    @Value("${Kafka.topics.userCreatedTopic}")
    private String userCreatedTopic;

    @Bean
    public NewTopic newUserTopic() {
        return new NewTopic(userTopic, 3, (short) 1);
    }

    @Bean
    public NewTopic newUserCreatedTopic() {
        return new NewTopic(userCreatedTopic, 3, (short) 1);
    }

}
