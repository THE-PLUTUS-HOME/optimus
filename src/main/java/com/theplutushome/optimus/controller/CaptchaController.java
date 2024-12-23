package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.entity.api.captcha.CaptchaRequest;
import com.theplutushome.optimus.entity.api.captcha.CaptchaResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5500")
public class CaptchaController {

    private static final String SECRET_KEY = "6LeG0KEqAAAAAKGtT0wQMtZh2WDHB9GFBBvM5I2X";

    @PostMapping("/verify-captcha")
    public ResponseEntity<?> verifyCaptcha(@RequestBody CaptchaRequest request, HttpServletRequest servletRequest) {
        System.out.println("Incoming Request Headers: " + servletRequest.getHeaderNames());
        System.out.println("Captcha Token: " + request.getToken());
        String captchaResponse = request.getToken();

        // Prepare the URL for Google's reCAPTCHA verification API
        String url = "https://www.google.com/recaptcha/api/siteverify";

        // Send the request to Google's API
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", SECRET_KEY);
        params.add("response", captchaResponse);

        ResponseEntity<CaptchaResponse> response = restTemplate.postForEntity(url, params, CaptchaResponse.class);

        // Check if reCAPTCHA validation succeeded
        if (response.getBody() != null && response.getBody().isSuccess()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "CAPTCHA verified successfully!"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "CAPTCHA verification failed!"
            ));
        }
    }
}
