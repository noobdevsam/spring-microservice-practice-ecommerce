package com.example.ecommerce.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main class for the Order Service application.
 * This class serves as the entry point for the Spring Boot application.
 */
@EnableFeignClients // Enables Feign clients for declarative REST client functionality.
@EnableJpaAuditing // Enables JPA auditing features such as @CreatedDate and @LastModifiedDate.
@SpringBootApplication // Marks this class as a Spring Boot application.
public class OrderServiceApplication {

    /**
     * Main method to launch the Order Service application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
