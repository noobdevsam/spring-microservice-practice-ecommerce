package com.example.ecommerce.customerservice.services.impl;

import com.example.ecommerce.customerservice.entities.Customer;
import com.example.ecommerce.customerservice.exceptions.CustomerNotFoundException;
import com.example.ecommerce.customerservice.mapper.CustomerMapper;
import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.model.CustomerResponseDTO;
import com.example.ecommerce.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        // This is necessary to inject the mocks into the service
        // and to avoid NullPointerExceptions.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllCustomers_ReturnsList() {
        var customer = Customer.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .build();
        var dto = new CustomerResponseDTO("1", "John", "Doe", "john@email.com", null);

        when(
                customerRepository.findAll()
        ).thenReturn(
                Collections.singletonList(customer)
        );
        when(
                customerMapper.customerToCustomerResponseDTO(customer)
        ).thenReturn(dto);

        var result = customerService.findAllCustomers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).firstName())
                .isEqualTo("John");
    }

    @Test
    void findCustomerById_Found() {
        var customer = Customer.builder()
                .id("1")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@email.com")
                .build();
        var dto = new CustomerResponseDTO("1", "Jane", "Smith", "jane@email.com", null);

        when(
                customerRepository.findById("1")
        ).thenReturn(
                Optional.of(customer)
        );
        when(
                customerMapper.customerToCustomerResponseDTO(customer)
        ).thenReturn(dto);

        var result = customerService.findCustomerById("1");
        assertThat(result.firstName())
                .isEqualTo("Jane");
    }

    @Test
    void findCustomerById_NotFound() {
        when(
                customerRepository.findById("1")
        ).thenReturn(
                Optional.empty()
        );

        assertThatThrownBy(
                () -> customerService.findCustomerById("1")
        ).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void existsCustomerById_True() {
        when(
                customerRepository.findById("1")
        ).thenReturn(
                Optional.of(new Customer())
        );

        assertThat(customerService.existsCustomerById("1")).isTrue();
    }

    @Test
    void existsCustomerById_False() {
        when(
                customerRepository.findById("1")
        ).thenReturn(
                Optional.empty()
        );

        assertThat(customerService.existsCustomerById("1")).isFalse();
    }

    @Test
    void createCustomer_Success() {
        var dto = new CustomerRequestDTO(null, "Sam", "Wise", "sam@email.com", null);
        var customer = Customer.builder()
                .id("123")
                .firstName("Sam")
                .lastName("Wise")
                .email("sam@email.com")
                .build();

        when(
                customerMapper.customerRequestDTOToCustomer(dto)
        ).thenReturn(customer);
        when(
                customerRepository.save(any(Customer.class))
        ).thenReturn(customer);

        var id = customerService.createCustomer(dto);
        assertThat(id).isEqualTo("123");
    }

    @Test
    void updateCustomer_UpdatesFields() {
        var dto = new CustomerRequestDTO("1", "Updated", "Name", "updated@email.com", null);
        var customer = Customer.builder()
                .id("1")
                .firstName("Old")
                .lastName("Name")
                .email("old@email.com")
                .build();

        when(
                customerRepository.findById("1")
        ).thenReturn(
                Optional.of(customer)
        );
        when(
                customerRepository.save(any(Customer.class))
        ).thenReturn(customer);

        customerService.updateCustomer(dto);

        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_NotFound() {
        var dto = new CustomerRequestDTO("notfound", "A", "B", "c@email.com", null);

        when(
                customerRepository.findById("notfound")
        ).thenReturn(
                Optional.empty()
        );

        assertThatThrownBy(
                () -> customerService.updateCustomer(dto)
        ).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void deleteCustomerById_Success() {
        when(
                customerRepository.findById("1")
        ).thenReturn(
                Optional.of(new Customer())
        );

        doNothing().when(customerRepository).deleteById("1");

        customerService.deleteCustomerById("1");

        verify(customerRepository).deleteById("1");
    }

    @Test
    void deleteCustomerById_NotFound() {
        when(
                customerRepository.findById("notfound")
        ).thenReturn(
                Optional.empty()
        );

        assertThatThrownBy(
                () -> customerService.deleteCustomerById("notfound")
        ).isInstanceOf(CustomerNotFoundException.class);
    }

}

