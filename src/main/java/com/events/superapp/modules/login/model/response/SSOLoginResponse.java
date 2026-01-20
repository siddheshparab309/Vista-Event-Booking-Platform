package com.events.superapp.modules.login.model.response;

public record SSOLoginResponse(
        String accessToken,
        String email,
        String fullName,
        String role
) {
}
