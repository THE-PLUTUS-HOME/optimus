package com.theplutushome.optimus.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@PropertySource("classpath:application.properties")
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String EXPECTED_API_KEY; // Replace with your API key

    // Inject the Environment and retrieve the API key
    public ApiKeyFilter(Environment environment) {
        this.EXPECTED_API_KEY = environment.getProperty("application.api.key");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(API_KEY_HEADER);
        String path = request.getRequestURI();
        System.out.println("Incoming Headers:");
        Collections.list(request.getHeaderNames())
                .forEach(header -> System.out.println(header + ": " + request.getHeader(header)));

        // Exclude Swagger UI and related resources
        if (path.startsWith("/swagger-ui")
                || path.startsWith("/api-docs")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars")) {
            filterChain.doFilter(request, response);
            System.out.println("Request proceeded successfully."); // Debugging line
            return;
        }

        if (path.matches("/optimus/v1/api/cryptomus/callback")) {
            filterChain.doFilter(request, response);
            System.out.println("Cryptomus Callback proceeded successfully."); // Debugging line
            return;
        }

        if (path.matches("/optimus/v1/api/payment/callback")) {
            filterChain.doFilter(request, response);
            System.out.println("Hubtel Callback proceeded successfully."); // Debugging line
            return;
        }

        if (path.matches("/optimus/v1/api/payment/sms/callback")) {
            filterChain.doFilter(request, response);
            System.out.println("USSD Callback proceeded successfully."); // Debugging line
            return;
        }

        if (EXPECTED_API_KEY.equals(apiKey)) {
            // Allow request to proceed
            filterChain.doFilter(request, response);
            System.out.println("Request proceeded successfully."); // Debugging line
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Invalid API Key. Yawa");
        }
    }
}
