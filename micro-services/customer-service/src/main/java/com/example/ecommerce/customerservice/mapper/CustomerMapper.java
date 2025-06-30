package com.example.ecommerce.customerservice.mapper;

import com.example.ecommerce.customerservice.entities.Customer;
import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.model.CustomerResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer customerRequestToCustomer(CustomerRequestDTO customerRequestDTO) {

        if (customerRequestDTO == null) {
            return null;
        }

        return Customer.builder()
                .id(customerRequestDTO.id())
                .firstName(customerRequestDTO.firstName())
                .lastName(customerRequestDTO.lastName())
                .email(customerRequestDTO.email())
                .address(customerRequestDTO.address())
                .build();
    }

    public CustomerResponseDTO customerToCustomerResponseDTO(Customer customer) {

        if (customer == null) {
            return null;
        }

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress()
        );
    }
}
