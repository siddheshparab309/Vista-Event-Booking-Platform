package com.events.superapp.modules.login.controller;

import com.events.superapp.common.entity.User;
import com.events.superapp.common.model.SessionData;
import com.events.superapp.common.repository.UserRepository;
import com.events.superapp.modules.login.model.response.SSOLoginResponse;
import com.events.superapp.modules.login.service.LoginService;
import com.events.superapp.modules.login.service.SessionService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginController {
    private final LoginService loginService;
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${google.client.id}")
    private String googleClientId;

    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody Map<String, String> body) {
        String tokenString = body.get("token");
        try {
            Payload payload = verifyGoogleToken(tokenString);
            User user = loginService.processUserLoginData(
                    payload.getEmail(),
                    (String) payload.get("name"),
                    payload.getSubject()
            );
            String sessionKey = sessionService.createSession(user);
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, createCookie(sessionKey, 7 * 24 * 60 * 60))
                    .body(new SSOLoginResponse(sessionKey, user.getEmail(), user.getName(), user.getRole().toString()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/verifySession")
    public ResponseEntity<?> verifySession(@CookieValue(name = "session_key", required = false) String sessionKey) {
        return sessionService.getSession(sessionKey) != null ? ResponseEntity.ok().build() : ResponseEntity.status(401).build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "session_key", required = false) String sessionKey) {
        sessionService.deleteSession(sessionKey);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, createCookie("", 0)).build();
    }

    @PutMapping("/updateCity")
    public ResponseEntity<?> updateCity(@CookieValue(name = "session_key") String sessionKey, @RequestBody Map<String, String> request) {
        SessionData sessionData = (SessionData) redisTemplate.opsForValue().get(sessionKey);
        if (sessionData == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return userRepository.findByEmail(sessionData.getEmail()).map(user -> {
            user.setPreferredCity(request.get("city"));
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "City updated", "city", user.getPreferredCity()));
        }).orElse(ResponseEntity.notFound().build());
    }

    private String createCookie(String key, long maxAge) {
        return ResponseCookie.from("session_key", key)
                .httpOnly(true).secure(false).path("/").maxAge(maxAge).sameSite("Lax").build().toString();
    }

    private Payload verifyGoogleToken(String tokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(tokenString);
        if (idToken == null) {
            System.out.println("ERROR: Token verification returned null for ID: " + googleClientId);
            throw new Exception("Invalid ID token from Google");
        }
        return idToken.getPayload();
    }

}