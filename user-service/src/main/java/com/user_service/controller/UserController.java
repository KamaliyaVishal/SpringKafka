package com.user_service.controller;

import com.user_service.record.UserRequest;
import com.user_service.record.UserResponse;
import com.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

//    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserService userService;
    private final KafkaTemplate<Long, UserResponse> notifyUserCreated;

    @Value("${Kafka.topics.userTopic}")
    private String userTopic;

    @Value("${Kafka.topics.userCreatedTopic}")
    private String userCreatedTopic;

   /* @PostMapping("/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable String message) {
        for (int i = 0; i < 1000; i++) {
            kafkaTemplate.send(userTopic, "" + i % 2, message + "_" + i);
        }
        return ResponseEntity.ok("Message Queued ");
    }*/

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);

        //Notify the Notification-Service that user created
        notifyUserCreated.send(userCreatedTopic, response.id(), response);

        return ResponseEntity.ok(response);
    }

}
