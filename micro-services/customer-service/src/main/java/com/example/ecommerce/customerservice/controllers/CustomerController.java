package com.example.ecommerce.customerservice.controllers;

import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.model.CustomerResponseDTO;
import com.example.ecommerce.customerservice.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing customer-related operations.
 * Provides RESTful endpoints for CRUD operations on customers.
 */
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Retrieves a list of all customers.
     *
     * @return ResponseEntity containing a list of CustomerResponseDTO objects.
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> findAll() {
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return ResponseEntity containing the CustomerResponseDTO object.
     */
    @GetMapping("/{customer-id}")
    public ResponseEntity<CustomerResponseDTO> findById(
            @PathVariable("customer-id") String customerId
    ) {
        return ResponseEntity.ok(customerService.findCustomerById(customerId));
    }

    /**
     * Creates a new customer.
     *
     * @param customerRequestDTO The data for the new customer.
     * @return ResponseEntity containing the ID of the created customer.
     */
    @PostMapping
    public ResponseEntity<String> createCustomer(
            @RequestBody @Valid CustomerRequestDTO customerRequestDTO
    ) {
        return ResponseEntity.ok(customerService.createCustomer(customerRequestDTO));
    }

    /**
     * Updates an existing customer.
     *
     * @param customerRequestDTO The updated data for the customer.
     * @return ResponseEntity indicating the update was accepted.
     */
    @PutMapping
    public ResponseEntity<Void> updateCustomer(
            @RequestBody @Valid CustomerRequestDTO customerRequestDTO
    ) {
        customerService.updateCustomer(customerRequestDTO);
        return ResponseEntity.accepted().build();
    }

    /**
     * Checks if a customer exists by their ID.
     *
     * @param customerId The ID of the customer to check.
     * @return ResponseEntity containing a boolean indicating existence.
     */
    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable("customer-id") String customerId
    ) {
        return ResponseEntity.ok(customerService.existsCustomerById(customerId));
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param customerId The ID of the customer to delete.
     * @return ResponseEntity indicating the deletion was successful.
     */
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable("customer-id") String customerId
    ) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

}