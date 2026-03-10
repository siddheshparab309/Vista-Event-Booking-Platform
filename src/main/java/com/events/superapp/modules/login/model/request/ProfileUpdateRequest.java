package com.events.superapp.modules.login.model.request;

public record ProfileUpdateRequest(
        String email,
        String name,
        String mobileNo
) {
}
