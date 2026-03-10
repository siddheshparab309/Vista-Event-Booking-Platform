package com.events.superapp.modules.login.service;

import com.events.superapp.common.entity.User;
import com.events.superapp.common.repository.UserRepository;
import com.events.superapp.modules.login.model.request.ProfileUpdateRequest;
import com.events.superapp.modules.movies.entity.UserRole;
import lombok.RequiredArgsConstructor;
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

    public User updateProfile(ProfileUpdateRequest request) {
        var email = request.email();
        var name = request.name();
        var mobileNo = request.mobileNo();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with provided email!"));

        if (name != null && !name.isBlank()) user.setName(name);
        if (mobileNo != null && !mobileNo.isBlank()) user.setMobileNo(mobileNo);

        return userRepository.save(user);
    }

}