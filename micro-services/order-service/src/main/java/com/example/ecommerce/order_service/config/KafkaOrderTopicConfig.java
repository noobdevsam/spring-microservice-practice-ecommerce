package com.example.ecommerce.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for defining Kafka topics related to the order service.
 * This class provides beans for creating and configuring Kafka topics.
 */
@Configuration
public class KafkaOrderTopicConfig {

    /**
     * Creates a Kafka topic named "order-topic".
     * This topic is used for handling order-related events in the system.
     *
     * @return A NewTopic object representing the "order-topic".
     */
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder
                .name("order-topic") // Specifies the name of the Kafka topic.
                .build(); // Builds and returns the topic configuration.
    }
}
