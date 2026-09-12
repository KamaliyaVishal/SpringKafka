package com.user_service.record;

import com.user_service.entiry.User;

public record UserRequest(
        String name,
        String email
) {
    public User toEntity() {
        User user = new User();
        user.setEmail(this.email);
        user.setName(this.name);
        return user;
    }
}
