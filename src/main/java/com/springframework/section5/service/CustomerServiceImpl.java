package com.springframework.section5.service;

import com.springframework.section5.dto.CustomerDto;
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

	private final Map<UUID, CustomerDto> customerMap;

	public CustomerServiceImpl() {
		this.customerMap = new HashMap<>();

		CustomerDto customerDto1 = CustomerDto.builder()
			.id(UUID.randomUUID())
			.customerName("name1")
			.version(5)
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		CustomerDto customerDto2 = CustomerDto.builder()
			.id(UUID.randomUUID())
			.customerName("name2")
			.version(6)
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		CustomerDto customerDto3 = CustomerDto.builder()
			.id(UUID.randomUUID())
			.customerName("name3")
			.version(7)
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		customerMap.put(customerDto1.getId(), customerDto1);
		customerMap.put(customerDto2.getId(), customerDto2);
		customerMap.put(customerDto3.getId(), customerDto3);
	}

	@Override
	public List<CustomerDto> findAllCustomers() {
		return new ArrayList<>(customerMap.values());
	}

	@Override
	public Optional<CustomerDto> getCustomerById(final UUID id) {
		return Optional.of(customerMap.get(id));
	}

	@Override
	public CustomerDto saveCustomer(final CustomerDto customerDto) {
		UUID id = UUID.randomUUID();
		customerDto.setId(id);
		customerMap.putIfAbsent(id, customerDto);
		return customerDto;
	}

	@Override
	public void updateCustomerById(final UUID id, final CustomerDto customerDto) {
		CustomerDto existingCustomerDto = customerMap.get(id);
		existingCustomerDto.setCustomerName(customerDto.getCustomerName());
		existingCustomerDto.setVersion(customerDto.getVersion());
		existingCustomerDto.setCreatedDate(customerDto.getCreatedDate());
		existingCustomerDto.setLastModifiedDate(customerDto.getLastModifiedDate());
	}

	@Override
	public void deleteCustomerById(final UUID id) {
		customerMap.remove(id);
	}

	@Override
	public void patchCustomerById(final UUID id, final CustomerDto customerDto) {
		CustomerDto existingCustomerDto = customerMap.get(id);
		if (StringUtils.hasText(customerDto.getCustomerName())) {
			existingCustomerDto.setCustomerName(customerDto.getCustomerName());
		}
		if (customerDto.getCreatedDate() != null) {
			existingCustomerDto.setCreatedDate(customerDto.getCreatedDate());
		}
		if (customerDto.getLastModifiedDate() != null) {
			existingCustomerDto.setLastModifiedDate(customerDto.getLastModifiedDate());
		}
		if (customerDto.getVersion() != null) {
			existingCustomerDto.setVersion(customerDto.getVersion());
		}
	}
}
