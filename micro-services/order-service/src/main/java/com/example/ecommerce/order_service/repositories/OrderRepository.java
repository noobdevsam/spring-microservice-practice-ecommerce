package com.example.ecommerce.order_service.repositories;

import com.example.ecommerce.order_service.entities.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
    // Additional query methods can be defined here if needed
}
