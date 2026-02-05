package com.events.superapp.modules.login.service;

import com.events.superapp.common.entity.User;
import com.events.superapp.common.model.SessionData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final long SESSION_EXPIRY = 7 * 24 * 60 * 60; // 7 Days

    public String createSession(User user) {
        String sessionKey = UUID.randomUUID().toString();
        SessionData data = new SessionData(user.getEmail(), user.getPhoneNumber(), user.getName());
        redisTemplate.opsForValue().set(sessionKey, data, SESSION_EXPIRY, TimeUnit.SECONDS);
        return sessionKey;
    }

    public SessionData getSession(String sessionKey) {
        if (sessionKey == null) return null;
        return (SessionData) redisTemplate.opsForValue().get(sessionKey);
    }

    public void deleteSession(String sessionKey) {
        if (sessionKey != null) redisTemplate.delete(sessionKey);
    }
}