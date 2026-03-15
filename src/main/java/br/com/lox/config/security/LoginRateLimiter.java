package br.com.lox.config.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginRateLimiter {

    private static final int MAX_ATTEMPTS = 5;
    private static final long COOLDOWN_SECONDS = 5 * 60; // 5 minutos

    private final ConcurrentHashMap<String, AttemptInfo> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String ip) {
        var info = attempts.get(ip);
        if (info == null) return false;

        if (info.count >= MAX_ATTEMPTS) {
            if (Instant.now().isBefore(info.blockedUntil)) {
                return true;
            }
            attempts.remove(ip);
            return false;
        }
        return false;
    }

    public void registerFailure(String ip) {
        attempts.compute(ip, (key, info) -> {
            if (info == null) return new AttemptInfo(1, null);

            int newCount = info.count + 1;
            Instant blockedUntil = newCount >= MAX_ATTEMPTS
                    ? Instant.now().plusSeconds(COOLDOWN_SECONDS)
                    : null;
            return new AttemptInfo(newCount, blockedUntil);
        });
    }

    public void resetAttempts(String ip) {
        attempts.remove(ip);
    }

    private record AttemptInfo(int count, Instant blockedUntil) {}
}
