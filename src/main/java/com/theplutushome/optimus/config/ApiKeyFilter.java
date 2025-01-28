// ApiKeyFilter.java
package com.theplutushome.optimus.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
@PropertySource("classpath:application.properties")
public class ApiKeyFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String EXPECTED_API_KEY; // Replace with your API key

    // Inject the Environment and retrieve the API key
    public ApiKeyFilter(Environment environment) {
        String apiKey = environment.getProperty("application.api.key");
        if (apiKey == null) {
            throw new IllegalStateException("API key must be configured via application.api.key");
        }
        this.EXPECTED_API_KEY = apiKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        logger.info("ApiKeyFilter: doFilterInternal");
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        // Exclude specific paths from filtering
        if (isExcludedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // First, attempt API Key validation
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (EXPECTED_API_KEY.equals(apiKey)) {
            // Optionally, you can set authentication here if needed
            // For example, set a specific role or authority
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    "apiClient", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_API")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.info("ApiKeyFilter: doFilterInternal: Authentication set");
            filterChain.doFilter(request, response);
            return;
        }
        // If neither API Key nor JWT is valid, deny access
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Forbidden: Invalid API Key");
        logger.info("ApiKeyFilter: doFilterInternal: Forbidden");
    }

    // Helper method to check if the path should be excluded
    private boolean isExcludedPath(String path) {
        // Define excluded paths
        return List.of(
                "/swagger-ui",
                "/api-docs",
                "/v3/api-docs",
                "/swagger-resources",
                "/webjars",
                "/optimus/v1/api/cryptomus/callback",
                "/optimus/v1/api/payment/callback",
                "/optimus/v1/api/payment/redde/callback",
                "/optimus/v1/api/payment/sms/callback").stream()
                .anyMatch(path::startsWith);
    }
}
