package com.springframework.section5.controller;

import com.springframework.section5.model.Customer;
import com.springframework.section5.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {

	private final CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Customer> findAllCustomers() {
		return customerService.findAllCustomers();
	}

	@RequestMapping(value = "{customerId}", method = RequestMethod.GET)
	public Customer getCustomerById(@PathVariable("customerId") final UUID id) {
		return customerService.getCustomerById(id);
	}
}
