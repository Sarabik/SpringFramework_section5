package com.springframework.section5.service;

import com.springframework.section5.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

	List<Customer> findAllCustomers();

	Customer getCustomerById(UUID id);

}
