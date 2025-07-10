package com.example.ecommerce.customerservice.controllers;

import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.model.CustomerResponseDTO;
import com.example.ecommerce.customerservice.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the CustomerController class.
 * These tests verify the behavior of the controller methods using MockMvc.
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerResponseDTO customerResponseDTO;
    private CustomerRequestDTO customerRequestDTO;

    /**
     * Sets up test data before each test execution.
     */
    @BeforeEach
    void setUp() {
        customerResponseDTO = new CustomerResponseDTO("1", "John", "Doe", "john@email.com", null);
        customerRequestDTO = new CustomerRequestDTO(null, "John", "Doe", "john@email.com", null);
    }

    /**
     * Tests the retrieval of all customers.
     * Verifies that the controller returns a list of customers.
     *
     * @throws Exception if the request fails
     */
    @Test
    void findAll_ReturnsList() throws Exception {
        var customers = Collections.singletonList(customerResponseDTO);

        when(customerService.findAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    /**
     * Tests the retrieval of a customer by ID.
     * Verifies that the controller returns the correct customer details.
     *
     * @throws Exception if the request fails
     */
    @Test
    void findById_ReturnsCustomer() throws Exception {
        when(customerService.findCustomerById("1")).thenReturn(customerResponseDTO);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    /**
     * Tests the creation of a customer.
     * Verifies that the controller returns the ID of the newly created customer.
     *
     * @throws Exception if the request fails
     */
    @Test
    void createCustomer_ReturnsId() throws Exception {
        when(customerService.createCustomer(any(CustomerRequestDTO.class))).thenReturn("1");

        mockMvc.perform(
                        post("/api/v1/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customerRequestDTO))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    /**
     * Tests the update of a customer.
     * Verifies that the controller returns an HTTP status of Accepted.
     *
     * @throws Exception if the request fails
     */
    @Test
    void updateCustomer_ReturnsAccepted() throws Exception {
        doNothing().when(customerService).updateCustomer(any(CustomerRequestDTO.class));

        mockMvc.perform(
                        put("/api/v1/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customerRequestDTO))
                )
                .andExpect(status().isAccepted());
    }

    /**
     * Tests the existence check for a customer by ID.
     * Verifies that the controller correctly identifies existing and non-existing customers.
     *
     * @throws Exception if the request fails
     */
    @Test
    void existsById_ReturnsTrue() throws Exception {
        when(customerService.existsCustomerById("1")).thenReturn(true);

        mockMvc.perform(get("/api/v1/customers/exists/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Tests the deletion of a customer by ID.
     * Verifies that the controller returns an HTTP status of No Content.
     *
     * @throws Exception if the request fails
     */
    @Test
    void deleteCustomer_ReturnsNoContent() throws Exception {
        doNothing().when(customerService).deleteCustomerById("1");

        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());
    }

}