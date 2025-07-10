package com.example.ecommerce.customerservice.services.impl;

import com.example.ecommerce.customerservice.entities.Customer;
import com.example.ecommerce.customerservice.exceptions.CustomerNotFoundException;
import com.example.ecommerce.customerservice.mapper.CustomerMapper;
import com.example.ecommerce.customerservice.model.CustomerRequestDTO;
import com.example.ecommerce.customerservice.model.CustomerResponseDTO;
import com.example.ecommerce.customerservice.repository.CustomerRepository;
import com.example.ecommerce.customerservice.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the CustomerService interface.
 * Provides business logic for managing customers.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    /**
     * Retrieves all customers from the repository.
     *
     * @return a list of CustomerResponseDTO objects representing all customers
     */
    @Override
    public List<CustomerResponseDTO> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a customer by their ID.
     *
     * @param id the ID of the customer to find
     * @return a CustomerResponseDTO object representing the customer
     * @throws CustomerNotFoundException if no customer is found with the given ID
     */
    @Override
    public CustomerResponseDTO findCustomerById(String id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerResponseDTO)
                .orElseThrow(
                        () -> new CustomerNotFoundException(String.format("Customer not found with id: %s", id))
                );
    }

    /**
     * Checks if a customer exists by their ID.
     *
     * @param id the ID of the customer to check
     * @return true if the customer exists, false otherwise
     */
    @Override
    public Boolean existsCustomerById(String id) {
        return customerRepository.existsById(id);
    }

    /**
     * Creates a new customer in the repository.
     *
     * @param customerRequestDTO the data transfer object containing customer details
     * @return the ID of the newly created customer
     */
    @Override
    public String createCustomer(CustomerRequestDTO customerRequestDTO) {
        return customerRepository.save(
                customerMapper.customerRequestDTOToCustomer(customerRequestDTO)
        ).getId();
    }

    /**
     * Updates an existing customer in the repository.
     *
     * @param customerRequestDTO the data transfer object containing updated customer details
     * @throws CustomerNotFoundException if no customer is found with the given ID
     */
    @Override
    public void updateCustomer(CustomerRequestDTO customerRequestDTO) {
        var customer = customerRepository.findById(customerRequestDTO.id())
                .orElseThrow(
                        () -> new CustomerNotFoundException(String.format("Customer not found with id: %s", customerRequestDTO.id()))
                );
        // Merge updated details into the existing customer entity
        customerRepository.save(
                mergeCustomer(customer, customerRequestDTO)
        );
    }

    /**
     * Merges updated customer details into an existing customer entity.
     *
     * @param customer           the existing customer entity
     * @param customerRequestDTO the data transfer object containing updated customer details
     * @return the updated customer entity
     */
    @Override
    public Customer mergeCustomer(Customer customer, CustomerRequestDTO customerRequestDTO) {
        if (StringUtils.isNotBlank(customerRequestDTO.firstName())) {
            customer.setFirstName(customerRequestDTO.firstName());
        }

        if (StringUtils.isNotBlank(customerRequestDTO.lastName())) {
            customer.setLastName(customerRequestDTO.lastName());
        }

        if (StringUtils.isNotBlank(customerRequestDTO.email())) {
            customer.setEmail(customerRequestDTO.email());
        }

        if (customerRequestDTO.address() != null) {
            customer.setAddress(customerRequestDTO.address());
        }
        return customer;
    }

    /**
     * Deletes a customer by their ID.
     *
     * @param id the ID of the customer to delete
     * @throws CustomerNotFoundException if no customer is found with the given ID
     */
    @Override
    public void deleteCustomerById(String id) {
        if (!existsCustomerById(id)) {
            throw new CustomerNotFoundException(String.format("Customer not found with id: %s", id));
        }
        customerRepository.deleteById(id);
    }
}