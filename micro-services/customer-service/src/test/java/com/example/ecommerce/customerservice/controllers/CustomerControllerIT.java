package com.example.ecommerce.customerservice.controllers;

import com.example.ecommerce.customerservice.entities.Address;
import com.example.ecommerce.customerservice.exceptions.CustomerNotFoundException;
import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class CustomerControllerIT {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        customerRepository.deleteAll();
    }

    private CustomerRequestDTO buildCustomerRequest(String firstName, String lastName, String email) {
        Address address = Address.builder()
                .street("123 Main St")
                .houseNumber("1A")
                .zipCode("12345")
                .build();
        return new CustomerRequestDTO(null, firstName, lastName, email, address);
    }

    @Test
    void createAndGetCustomer() {
        var request = buildCustomerRequest("John", "Doe", "john@email.com");
        var id = customerController.createCustomer(request).getBody();

        assertThat(id).isNotBlank();

        var response = customerController.findById(id);

        assertThat(response.getBody().firstName())
                .isEqualTo("John");
        assertThat(response.getBody().lastName())
                .isEqualTo("Doe");
        assertThat(response.getBody().email())
                .isEqualTo("john@email.com");
    }

    @Test
    void getAllCustomers() {
        customerController.createCustomer(buildCustomerRequest("Alice", "Smith", "alice@email.com"));
        customerController.createCustomer(buildCustomerRequest("Bob", "Brown", "bob@email.com"));

        var all = customerController.findAll().getBody();

        assertThat(all).hasSize(2);
    }

    @Test
    void updateCustomer() {
        var id = customerController.createCustomer(buildCustomerRequest("Jane", "Roe", "jane@email.com")).getBody();
        var update = new CustomerRequestDTO(id, "Janet", "Roe", "janet@email.com", null);

        customerController.updateCustomer(update);

        var updated = customerController.findById(id).getBody();

        assertThat(updated.firstName())
                .isEqualTo("Janet");
        assertThat(updated.email())
                .isEqualTo("janet@email.com");
    }

    @Test
    void existsById() {
        var id = customerController.createCustomer(buildCustomerRequest("Sam", "Wise", "sam@email.com")).getBody();

        assertThat(customerController.existsById(id).getBody()).isTrue();
        assertThat(customerController.existsById("fakeid").getBody()).isFalse();
    }

    @Test
    void deleteCustomer() {
        var id = customerController.createCustomer(buildCustomerRequest("Tom", "Thumb", "tom@email.com")).getBody();

        customerController.deleteCustomer(id);

        var deleted = customerRepository.findById(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    void getCustomer_NotFound() {
        assertThatThrownBy(
                () -> customerController.findById("notfound")
        ).isInstanceOf(CustomerNotFoundException.class);
    }

}