package com.afiya.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * This creates a @LoadBalanced WebClient.Builder.
     * The @LoadBalanced annotation is CRITICAL. It tells Spring Cloud
     * to use Eureka to resolve service names (like "http://auth-ms")
     * into actual host and port combinations (like "http://localhost:8085").
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}

