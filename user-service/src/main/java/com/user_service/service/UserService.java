package com.user_service.service;

import com.user_service.entiry.User;
import com.user_service.record.UserRequest;
import com.user_service.record.UserResponse;
import com.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserRequest request) {

        User user = request.toEntity();

        User savedUser = userRepository.save(user);

        return UserResponse.fromEntity(savedUser);
    }
}
