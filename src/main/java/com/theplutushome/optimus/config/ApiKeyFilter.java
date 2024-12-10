package com.theplutushome.optimus.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String EXPECTED_API_KEY = "your-api-key"; // Replace with your API key

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(API_KEY_HEADER);
        System.out.println("API Key Received: " + apiKey); // Debugging line

        if (EXPECTED_API_KEY.equals(apiKey)) {
            // Allow request to proceed
            filterChain.doFilter(request, response);
            System.out.println("Request proceeded successfully."); // Debugging line
        } else {
            // Respond with 403 Forbidden if API key is missing or invalid
            System.out.println("Invalid API Key or Missing API Key"); // Debugging line
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Invalid API Key");
        }
    }
}
