package com.events.superapp.modules.login.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SSOLoginResponse {
    @NotNull
    private String sessionKey;
    private String email;
    private String name;
    private String role;
}