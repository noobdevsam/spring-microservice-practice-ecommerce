package com.example.ecommerce.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka topics related to the order service.
 * Defines beans for creating Kafka topics.
 */
@Configuration
public class KafkaOrderTopicConfig {

    /**
     * Creates a new Kafka topic named "order-topic".
     *
     * @return A NewTopic object representing the "order-topic".
     */
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("order-topic") // Sets the name of the topic
                .build(); // Builds the topic configuration
    }
}
