package com.springframework.section5.service;

import com.springframework.section5.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

	List<Customer> findAllCustomers();

	Customer getCustomerById(UUID id);

	Customer saveCustomer(Customer customer);

	void updateCustomerById(UUID id, Customer customer);

	void deleteCustomerById(UUID id);

	void patchCustomerById(UUID id, Customer customer);
}
