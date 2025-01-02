package com.theplutushome.optimus.service;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@PropertySource("classpath:application.properties")
@Service
public class EmailService {

    private static final String API_URL = "https://api.smtp2go.com/v3/email/send";
    private static final String API_KEY = "api-000052B725F74C899C8E1E9593529ABA";
    private static final String SENDER_EMAIL = "info@theplutushome.com";

    private final WebClient webClient;

    public EmailService(WebClient.Builder webClientBuilder, Environment env) {
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
