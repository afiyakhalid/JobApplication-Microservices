package com.afiya.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class JwtAuthGatewayFilterFactory
        extends AbstractGatewayFilterFactory<JwtAuthGatewayFilterFactory.Config> {

    private final WebClient.Builder webClientBuilder;

    public JwtAuthGatewayFilterFactory(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest req = exchange.getRequest();
            String path = req.getURI().getPath();

            // 1. Skip if the path is for the auth service
            if (path.startsWith("/auth")) {
                return chain.filter(exchange);
            }

            // 2. Check for the Authorization header
            String authHeader = req.getHeaders().getFirst("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return reject(exchange, "Missing or invalid Authorization header");
            }

            // 3. Send the token to auth-ms for validation
            return webClientBuilder.build().post()
                    .uri("http://auth-ms/auth/validate") // Use service name (thanks to @LoadBalanced)
                    .header("Authorization", authHeader)
                    .retrieve()
                    // 4. ***THIS IS THE FIX***: Read the username from the BODY, not a header
                    .bodyToMono(Map.class) // Expect a Map, e.g., {"username": "afiya"}
                    .flatMap(responseBody -> {
                        // 5. Get username from the response body
                        String username = (String) responseBody.get("username");
                        if (username == null || username.isEmpty()) {
                            return reject(exchange, "Invalid token: username not found in response");
                        }

                        // 6. Add the username as a header for downstream services
                        ServerHttpRequest mutatedReq = req.mutate()
                                .header("X-User-Id", username) // Pass username downstream
                                .build();

                        return chain.filter(exchange.mutate().request(mutatedReq).build());
                    })
                    .onErrorResume(e -> {
                        // Handle 401s or other errors from the auth-ms call
                        return reject(exchange, "Invalid token: Validation failed");
                    });
        };
    }

    // Helper method to send a 401 Unauthorized response
    private Mono<Void> reject(ServerWebExchange exchange, String msg) {
        ServerHttpResponse res = exchange.getResponse();
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        return res.writeWith(
                Mono.just(res.bufferFactory().wrap(msg.getBytes(StandardCharsets.UTF_8)))
        );
    }

    // Empty config class, as we don't need specific per-route configs
    public static class Config {}
}
