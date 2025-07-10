package com.example.ecommerce.payment_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka topics related to the Payment Service.
 * Defines the topics used for payment notifications.
 */
@Configuration
public class KafkaPaymentTopicConfig {

    /**
     * Creates a Kafka topic named "payment-topic".
     * This topic is used for sending payment notifications.
     *
     * @return a {@link NewTopic} object representing the "payment-topic".
     */
    @Bean
    public NewTopic paymentNotificationTopic() {
        return TopicBuilder
                .name("payment-topic")
                .build();
    }
}
