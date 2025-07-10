package com.example.ecommerce.gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Entry point for the Gateway Service application.
 * This service acts as a gateway for routing and securing requests in a microservices architecture.
 */
@EnableWebFluxSecurity
@SpringBootApplication
public class GatewayServiceApplication {

    /**
     * Main method to start the Gateway Service application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    /**
     * Configures the security filter chain for the application.
     * <p>
     * - Disables CSRF protection.
     * - Allows unauthenticated access to paths matching `/eureka/**`.
     * - Requires authentication for all other exchanges.
     * - Configures OAuth2 resource server to use JWT for authentication.
     *
     * @param http the ServerHttpSecurity object used to configure security settings
     * @return the configured SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http
    ) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        authorize ->
                                authorize.pathMatchers("/eureka/**").permitAll()
                                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(Customizer.withDefaults())
                )
                .build();
    }

}