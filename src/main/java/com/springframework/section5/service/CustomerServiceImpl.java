package com.springframework.section5.service;

import com.springframework.section5.model.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final Map<UUID, Customer> customerMap;

	public CustomerServiceImpl() {
		this.customerMap = new HashMap<>();

		Customer customer1 = Customer.builder()
			.id(UUID.randomUUID())
			.customerName("name1")
			.version(5)
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		Customer customer2 = Customer.builder()
			.id(UUID.randomUUID())
			.customerName("name2")
			.version(6)
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		Customer customer3 = Customer.builder()
			.id(UUID.randomUUID())
			.customerName("name3")
			.version(7)
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		customerMap.put(customer1.getId(), customer1);
		customerMap.put(customer2.getId(), customer2);
		customerMap.put(customer3.getId(), customer3);
	}

	@Override
	public List<Customer> findAllCustomers() {
		return new ArrayList<>(customerMap.values());
	}

	@Override
	public Customer getCustomerById(final UUID id) {
		return customerMap.get(id);
	}
}
