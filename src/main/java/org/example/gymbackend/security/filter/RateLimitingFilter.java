package org.example.gymbackend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.*;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final int loginAttemptsPerMinute;
    private final ObjectMapper objectMapper;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimitingFilter(@Value("${rate.limit.login-per-minute}") int loginAttemptsPerMinute,
                              ObjectMapper objectMapper) {
        this.loginAttemptsPerMinute = loginAttemptsPerMinute;
        this.objectMapper = objectMapper;
    }

    private Bucket createBucket() {
        Refill refill = Refill.intervally(loginAttemptsPerMinute, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(loginAttemptsPerMinute, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket getBucket(String key) {
        return buckets.computeIfAbsent(key, k -> createBucket());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        boolean isLoginRequest = "POST".equalsIgnoreCase(request.getMethod())
                && "/auth/login".equals(request.getServletPath());

        if (!isLoginRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        Bucket bucket = getBucket(request.getRemoteAddr());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(429);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(
                response.getWriter(),
                Map.of("error", "Too many login attempts. Please try again in a minute.")
        );
    }
}