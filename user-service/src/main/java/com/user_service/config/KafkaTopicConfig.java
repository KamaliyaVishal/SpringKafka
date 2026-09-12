package com.user_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${Kafka.topics.usertopic}")
    private String userTopic;

    @Bean
    public NewTopic newTopic() {
        return new NewTopic(userTopic, 3, (short) 1);
    }

}
