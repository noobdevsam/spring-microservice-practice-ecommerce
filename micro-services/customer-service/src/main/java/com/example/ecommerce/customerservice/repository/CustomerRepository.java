package com.example.ecommerce.customerservice.repository;

import com.example.ecommerce.customerservice.entities.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
