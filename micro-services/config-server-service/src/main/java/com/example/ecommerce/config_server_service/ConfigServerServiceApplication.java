package com.example.ecommerce.config_server_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer // Enable the Config Server functionality
@SpringBootApplication
public class ConfigServerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerServiceApplication.class, args);
    }

}
