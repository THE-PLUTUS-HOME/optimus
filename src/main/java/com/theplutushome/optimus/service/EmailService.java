package com.theplutushome.optimus.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@PropertySource("classpath:application.properties")
@Service
public class EmailService {

    private final String API_KEY;
    private static final String SENDER_EMAIL = "verify@theplutushome.com";

    private final WebClient webClient;

    public EmailService(WebClient.Builder webClientBuilder, Environment env) {
        String API_URL = env.getProperty("smtp2go_api_url");
        this.API_KEY = env.getProperty("smtp2go_api_key");
        assert API_URL != null;
        this.webClient = webClientBuilder.baseUrl(API_URL).build();
    }

    public void sendEmail(String to, String subject, String htmlBody) {
        EmailRequest emailRequest = new EmailRequest(SENDER_EMAIL, to, subject, htmlBody);

        webClient.post()
                .header("Content-Type", "application/json")
                .header("X-Smtp2go-Api-Key", API_KEY)
                .bodyValue(emailRequest)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    throw new RuntimeException("Failed to send email", error);
                })
                .subscribe();
    }

    // Inner class for the email request body
    static class EmailRequest {
        private final String sender;
        private final String[] to;
        private final String subject;
        private final String html_body;

        public EmailRequest(String sender, String to, String subject, String htmlBody) {
            this.sender = sender;
            this.to = new String[]{to};
            this.subject = subject;
            this.html_body = htmlBody;
        }

        public String getSender() {
            return sender;
        }

        public String[] getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public String getHtml_body() {
            return html_body;
        }
    }
}
