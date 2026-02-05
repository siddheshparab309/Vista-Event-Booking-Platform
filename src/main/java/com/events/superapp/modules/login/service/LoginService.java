package com.events.superapp.modules.login.service;

import com.events.superapp.common.entity.User;
import com.events.superapp.common.repository.UserRepository;
import com.events.superapp.modules.movies.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    public User processUserLoginData(String email, String name, String googleId) {
        return findOrCreateUser(email, name, googleId);
    }

    private User findOrCreateUser(String email, String name, String googleId) {
        if (email == null) {
            throw new RuntimeException("Email not provided by Google");
        }
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setSsoId(googleId);
            newUser.setRole(UserRole.ROLE_USER);
            return userRepository.save(newUser);
        });
    }
}