package com.example.ecommerce.config_discovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // Enables Eureka Server functionality
@SpringBootApplication
public class ConfigDiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigDiscoveryServiceApplication.class, args);
    }

}
