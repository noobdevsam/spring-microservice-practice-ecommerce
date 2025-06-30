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

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerResponseDTO> findAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO findCustomerById(String id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerResponseDTO)
                .orElseThrow(
                        () -> new CustomerNotFoundException(String.format("Customer not found with id: %s", id))
                );
    }

    @Override
    public Boolean existsCustomerById(String id) {
        return customerRepository.findById(id).isPresent();
    }

    @Override
    public String createCustomer(CustomerRequestDTO customerRequestDTO) {
        return customerRepository.save(
                customerMapper.customerRequestDTOToCustomer(customerRequestDTO)
        ).getId();
    }

    @Override
    public void updateCustomer(CustomerRequestDTO customerRequestDTO) {
        var customer = customerRepository.findById(customerRequestDTO.id())
                .orElseThrow(
                        () -> new CustomerNotFoundException(String.format("Customer not found with id: %s", customerRequestDTO.id()))
                );

        mergeCustomer(customer, customerRequestDTO);
        customerRepository.save(customer);
    }

    @Override
    public void mergeCustomer(Customer customer, CustomerRequestDTO customerRequestDTO) {
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
    }

    @Override
    public void deleteCustomerById(String id) {
        if (!existsCustomerById(id)) {
            throw new CustomerNotFoundException(String.format("Customer not found with id: %s", id));
        }
        customerRepository.deleteById(id);
    }
}
