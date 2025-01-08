package com.theplutushome.optimus.controller;

import com.theplutushome.optimus.entity.api.captcha.CaptchaRequest;
import com.theplutushome.optimus.entity.api.captcha.CaptchaResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@PropertySource("classpath:application.properties")
@RestController
@RequestMapping("/api")
public class CaptchaController {

    private final String captchaKey;

    @Autowired
    public CaptchaController(Environment env) {
        this.captchaKey = env.getProperty("captchaKey");
    }

    @PostMapping("/verify-captcha")
    public ResponseEntity<?> verifyCaptcha(@RequestBody CaptchaRequest request, HttpServletRequest servletRequest) {
        String captchaResponse = request.getToken();

        // Prepare the URL for Google's reCAPTCHA verification API
        String url = "https://www.google.com/recaptcha/api/siteverify";

        // Send the request to Google's API
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", captchaKey);
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
