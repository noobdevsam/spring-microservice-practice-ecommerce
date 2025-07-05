package com.example.ecommerce.payment_service.repositories;

import com.example.ecommerce.payment_service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
