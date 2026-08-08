package org.example.gymbackend.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limits POST /auth/login by client IP address, using Bucket4j.
 * Configured via rate.limit.login-per-minute (see application.yaml / .env's
 * RATE_LIMIT_LOGIN_PER_MINUTE) - this property already existed but nothing
 * was reading it before this filter was added.
 *
 * Scoped to login specifically (not register or other endpoints) since
 * brute-forcing login credentials is the concrete abuse case this defends
 * against, matching the .env variable's name and the arc42 doc's intent.
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final int loginAttemptsPerMinute;
    private final ConcurrentHashMap<String, Bucket> bucketsByIp = new ConcurrentHashMap<>();

    public RateLimitingFilter(@Value("${rate.limit.login-per-minute:5}") int loginAttemptsPerMinute) {
        this.loginAttemptsPerMinute = loginAttemptsPerMinute;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        boolean isLoginRequest = "POST".equalsIgnoreCase(request.getMethod())
                && "/auth/login".equals(request.getServletPath());

        if (!isLoginRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        Bucket bucket = bucketsByIp.computeIfAbsent(request.getRemoteAddr(), ip ->
                Bucket.builder()
                        .addLimit(Bandwidth.simple(loginAttemptsPerMinute, Duration.ofMinutes(1)))
                        .build());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many login attempts. Please try again in a minute.\"}");
        }
    }
}
