package com.theplutushome.optimus.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.theplutushome.optimus.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (HttpMethod.OPTIONS.matches(request.getMethod()) || isExcludedPath(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT from cookies
        String jwt = extractJwtFromCookies(request);
        logger.info("JwtFilter: doFilterInternal: JWT: " + jwt);
        if (jwt != null && jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.extractClaim(jwt).getSubject();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.info("JwtFilter: doFilterInternal: Authentication set");
        } else {
            logger.info("JwtFilter: doFilterInternal: Authentication not set");
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        logger.info("JwtFilter: extractJwtFromCookies: " + request.toString());
        if (request.getCookies() == null) {
            logger.info("JwtFilter: extractJwtFromCookies: No cookies");
            return null;
        }
        Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "JWT".equals(cookie.getName()))
                .findFirst();
        return jwtCookie.map(Cookie::getValue).orElse(null);

    }

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
