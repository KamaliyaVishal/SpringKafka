package com.notification_service.consumer;

import com.user_service.record.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserKafkaConsumer {

    @KafkaListener(topics = "${Kafka.topics.userTopic}")
    public void subscribeUserTopic_first(String message) {
        log.info("Message received in subscribeUserTopic_first: {}", message);
    }

    @KafkaListener(topics = "${Kafka.topics.userTopic}")
    public void subscribeUserTopic_second(String message) {
        log.info("Message received in subscribeUserTopic_second: {}", message);
    }

    @KafkaListener(topics = "${Kafka.topics.userTopic}")
    public void subscribeUserTopic_third(String message) {
        log.info("Message received in subscribeUserTopic_third: {}", message);
    }

    @KafkaListener(topics = "${Kafka.topics.userCreatedTopic}", groupId = "${spring.application.name}")
    public void subscribeUserCreatedTopic(UserResponse message) {
        log.info("Message received in subscribeUserCreatedTopic: {}", message);
    }
}
