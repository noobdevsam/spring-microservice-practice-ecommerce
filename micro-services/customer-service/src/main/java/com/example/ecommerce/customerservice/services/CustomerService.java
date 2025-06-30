package com.example.ecommerce.customerservice.services;

import com.example.ecommerce.customerservice.entities.Customer;
import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.model.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDTO> findAllCustomers();

    CustomerResponseDTO findCustomerById(String id);

    Boolean existsCustomerById(String id);

    String createCustomer(CustomerRequestDTO customerRequestDTO);

    void updateCustomer(CustomerRequestDTO customerRequestDTO);

    void mergeCustomer(Customer customer, CustomerRequestDTO customerRequestDTO);

    void deleteCustomerById(String id);
}
