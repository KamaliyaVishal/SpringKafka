package com.user_service.record;


public record UserResponse(
    Long id,
    String name,
    String email
) {}
