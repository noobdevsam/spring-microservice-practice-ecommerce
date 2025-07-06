package com.example.ecommerce.notification_service.repositories;

import com.example.ecommerce.notification_service.entities.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    // Additional query methods can be defined here if needed
}
