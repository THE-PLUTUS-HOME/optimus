//package com.theplutushome.optimus.config;
//
//import org.apache.hc.client5.http.auth.AuthScope;
//import org.apache.hc.client5.http.auth.Credentials;
//import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
//import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
//import org.apache.hc.core5.http.HttpHost;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateCustomizer;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Objects;
//
//@PropertySource("classpath:application.properties")
//@Component
//public class ProxyAuthCustomizer implements RestTemplateCustomizer {
//    private static final Logger log = LoggerFactory.getLogger(ProxyAuthCustomizer.class);
//
//    private final String PROXY_SERVER_HOST;
//    private final int PROXY_SERVER_PORT;
//    private final String PROXY_USERNAME;
//    private final String PROXY_PASSWORD;
//
//    @Autowired
//    public ProxyAuthCustomizer(Environment env) {
//        this.PROXY_SERVER_HOST = Objects.requireNonNull(env.getProperty("proxy_server_host"), "Proxy server host is required");
//        this.PROXY_USERNAME = Objects.requireNonNull(env.getProperty("proxy_server_username"), "Proxy username is required");
//        this.PROXY_PASSWORD = Objects.requireNonNull(env.getProperty("proxy_server_password"), "Proxy password is required");
//        this.PROXY_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(env.getProperty("proxy_server_port")));
//    }
//
//    @Override
//    public void customize(RestTemplate restTemplate) {
//        System.out.println("Configuring RestTemplate to use proxy: " + PROXY_SERVER_HOST + ":" + PROXY_SERVER_PORT + " with user: " + PROXY_USERNAME);
//        log.info("Configuring RestTemplate to use proxy: {}:{} with user: {}", PROXY_SERVER_HOST, PROXY_SERVER_PORT, PROXY_USERNAME);
//
//        // Define proxy
//        HttpHost proxy = new HttpHost(PROXY_SERVER_HOST, PROXY_SERVER_PORT);
//        AuthScope authScope = new AuthScope(PROXY_SERVER_HOST, PROXY_SERVER_PORT);
//
//
//        // Set credentials for the proxy
//        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        Credentials proxyCredentials = new UsernamePasswordCredentials(PROXY_USERNAME, PROXY_PASSWORD.toCharArray());
//        credentialsProvider.setCredentials(authScope, proxyCredentials);
//
//        // Configure HttpClient with proxy and credentials
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setRoutePlanner(new DefaultProxyRoutePlanner(proxy))
//                .setDefaultCredentialsProvider(credentialsProvider)
//                .build();
//
//        // Set HttpClient in RestTemplate
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
//    }
//}
