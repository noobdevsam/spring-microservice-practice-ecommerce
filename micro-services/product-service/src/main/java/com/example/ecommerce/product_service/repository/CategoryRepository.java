package com.example.ecommerce.product_service.repository;

import com.example.ecommerce.product_service.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Additional query methods can be defined here if needed
}
