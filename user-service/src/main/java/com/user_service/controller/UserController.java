package com.user_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${Kafka.topics.usertopic}")
    private String userTopic;


    @PostMapping("/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable String message) {
        for (int i = 0; i < 1000; i++) {
            kafkaTemplate.send(userTopic, "" + i % 2, message + "_" + i);
        }
        return ResponseEntity.ok("Message Queued ");
    }

}
