package com.springframework.section5.service;

import com.springframework.section5.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
	public Optional<Customer> getCustomerById(final UUID id) {
		return Optional.of(customerMap.get(id));
	}

	@Override
	public Customer saveCustomer(final Customer customer) {
		UUID id = UUID.randomUUID();
		customer.setId(id);
		customerMap.putIfAbsent(id, customer);
		return customer;
	}

	@Override
	public void updateCustomerById(final UUID id, final Customer customer) {
		Customer existingCustomer = customerMap.get(id);
		existingCustomer.setCustomerName(customer.getCustomerName());
		existingCustomer.setVersion(customer.getVersion());
		existingCustomer.setCreatedDate(customer.getCreatedDate());
		existingCustomer.setLastModifiedDate(customer.getLastModifiedDate());
	}

	@Override
	public void deleteCustomerById(final UUID id) {
		customerMap.remove(id);
	}

	@Override
	public void patchCustomerById(final UUID id, final Customer customer) {
		Customer existingCustomer = customerMap.get(id);
		if (StringUtils.hasText(customer.getCustomerName())) {
			existingCustomer.setCustomerName(customer.getCustomerName());
		}
		if (customer.getCreatedDate() != null) {
			existingCustomer.setCreatedDate(customer.getCreatedDate());
		}
		if (customer.getLastModifiedDate() != null) {
			existingCustomer.setLastModifiedDate(customer.getLastModifiedDate());
		}
		if (customer.getVersion() != null) {
			existingCustomer.setVersion(customer.getVersion());
		}
	}
}
