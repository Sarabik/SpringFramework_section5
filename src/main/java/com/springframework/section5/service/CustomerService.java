package com.springframework.section5.service;

import com.springframework.section5.dto.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

	List<CustomerDto> findAllCustomers();

	CustomerDto getCustomerById(UUID id);

	CustomerDto saveCustomer(CustomerDto customerDto);

	void updateCustomerById(UUID id, CustomerDto customerDto);

	void deleteCustomerById(UUID id);

	void patchCustomerById(UUID id, CustomerDto customerDto);
}
