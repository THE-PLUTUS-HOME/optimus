// ApiKeyFilter.java
package com.theplutushome.optimus.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import com.theplutushome.optimus.util.JwtUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
@PropertySource("classpath:application.properties")
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String EXPECTED_API_KEY; // Replace with your API key
    private final JwtUtil jwtUtil;

    // Define paths that should be excluded from both API Key and JWT validation
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/swagger-ui",
            "/api-docs",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars",
            "/optimus/v1/api/cryptomus/callback",
            "/optimus/v1/api/payment/callback",
            "/optimus/v1/api/payment/redde/callback",
            "/optimus/v1/api/payment/sms/callback"
    );

    // Inject the Environment and retrieve the API key
    public ApiKeyFilter(Environment environment, JwtUtil jwtUtil) {
        String apiKey = environment.getProperty("application.api.key");
        if (apiKey == null) {
            throw new IllegalStateException("API key must be configured via application.api.key");
        }
        this.EXPECTED_API_KEY = apiKey;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
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
            filterChain.doFilter(request, response);
            return;
        }

        // If API Key validation fails, attempt JWT validation
        String jwt = jwtUtil.extractJwtFromRequest(request);
        if (jwt != null && jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.extractClaim(jwt).getSubject();
            // Set authentication in the context
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
            return;
        }

        // If neither API Key nor JWT is valid, deny access
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Forbidden: Invalid API Key or JWT");
    }

    // Helper method to check if the path should be excluded
    private boolean isExcludedPath(String path) {
        for (String excludedPath : EXCLUDED_PATHS) {
            if (path.startsWith(excludedPath)) {
                return true;
            }
        }
        return false;
    }
}
