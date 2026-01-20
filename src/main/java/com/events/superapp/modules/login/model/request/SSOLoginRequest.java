package com.events.superapp.modules.login.model.request;

public record SSOLoginRequest(
        String idToken,
        String email,
        String provider
) {
}
