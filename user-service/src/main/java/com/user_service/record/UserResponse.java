package com.user_service.record;

import com.user_service.entiry.User;

public record UserResponse(
        Long id,
        String name,
        String email
) {
    public static UserResponse fromEntity(User entity) {
        if (entity == null) return null;

        return new UserResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }
}
