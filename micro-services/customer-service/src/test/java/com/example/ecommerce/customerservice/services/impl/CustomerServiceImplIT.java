package com.example.ecommerce.customerservice.services.impl;

import com.example.ecommerce.customerservice.entities.Address;
import com.example.ecommerce.customerservice.exceptions.CustomerNotFoundException;
import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.repository.CustomerRepository;
import com.example.ecommerce.customerservice.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for the CustomerServiceImpl class.
 * These tests verify the behavior of the service methods in a Spring Boot test environment.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerServiceImplIT {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Cleans the database by deleting all records before each test.
     */
    @BeforeEach
    void cleanDb() {
        customerRepository.deleteAll();
    }

    /**
     * Builds a CustomerRequestDTO object for testing purposes.
     *
     * @param firstName the first name of the customer
     * @param lastName  the last name of the customer
     * @param email     the email of the customer
     * @return a CustomerRequestDTO object with the provided details
     */
    private CustomerRequestDTO buildCustomerRequest(String firstName, String lastName, String email) {
        var address = Address.builder()
                .street("123 Main St")
                .zipCode("12345")
                .houseNumber("6546545")
                .build();
        return new CustomerRequestDTO(null, firstName, lastName, email, address);
    }

    /**
     * Tests the creation and retrieval of a customer.
     * Verifies that the customer is created and can be retrieved with the correct details.
     */
    @Test
    void createAndFindCustomer_Success() {
        var request = buildCustomerRequest("John", "Doe", "john.doe@email.com");
        var id = customerService.createCustomer(request);

        assertThat(id).isNotBlank();

        var found = customerService.findCustomerById(id);

        assertThat(found.firstName())
                .isEqualTo("John");
        assertThat(found.lastName())
                .isEqualTo("Doe");
        assertThat(found.email())
                .isEqualTo("john.doe@email.com");
    }

    /**
     * Tests the retrieval of all customers.
     * Verifies that all created customers are returned.
     */
    @Test
    void findAllCustomers_ReturnsList() {
        customerService.createCustomer(
                buildCustomerRequest("Alice", "Smith", "alice@email.com")
        );
        customerService.createCustomer(
                buildCustomerRequest("Bob", "Brown", "bob@email.com")
        );

        var all = customerService.findAllCustomers();

        assertThat(all).hasSize(2);
    }

    /**
     * Tests the update functionality of a customer.
     * Verifies that the customer details are updated correctly.
     */
    @Test
    void updateCustomer_UpdatesFields() {
        var id = customerService.createCustomer(
                buildCustomerRequest("Jane", "Roe", "jane@email.com")
        );
        var update = new CustomerRequestDTO(id, "Janet", "Roe", "janet@email.com", null);

        customerService.updateCustomer(update);

        var updated = customerService.findCustomerById(id);

        assertThat(updated.firstName())
                .isEqualTo("Janet");
        assertThat(updated.email())
                .isEqualTo("janet@email.com");
    }

    /**
     * Tests the deletion of a customer by ID.
     * Verifies that the customer is removed from the repository.
     */
    @Test
    void deleteCustomerById_RemovesCustomer() {
        var id = customerService.createCustomer(
                buildCustomerRequest("Tom", "Thumb", "tom@email.com")
        );

        customerService.deleteCustomerById(id);

        assertThatThrownBy(
                () -> customerService.findCustomerById(id)
        ).isInstanceOf(CustomerNotFoundException.class);
    }

    /**
     * Tests the retrieval of a non-existing customer.
     * Verifies that a CustomerNotFoundException is thrown.
     */
    @Test
    void findCustomerById_NotFound_Throws() {
        assertThatThrownBy(
                () -> customerService.findCustomerById("nonexistent")
        ).isInstanceOf(CustomerNotFoundException.class);
    }

    /**
     * Tests the existence check for a customer by ID.
     * Verifies that the method correctly identifies existing and non-existing customers.
     */
    @Test
    void existsCustomerById_Works() {
        var id = customerService.createCustomer(
                buildCustomerRequest("Sam", "Wise", "sam@email.com")
        );

        assertThat(customerService.existsCustomerById(id)).isTrue();
        assertThat(customerService.existsCustomerById("fakeid")).isFalse();
    }

}