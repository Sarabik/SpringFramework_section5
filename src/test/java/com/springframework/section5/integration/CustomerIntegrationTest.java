package com.springframework.section5.integration;

import com.springframework.section5.controller.CustomerController;
import com.springframework.section5.controller.NotFoundException;
import com.springframework.section5.dto.CustomerDto;
import com.springframework.section5.entity.Customer;
import com.springframework.section5.mapper.CustomerMapper;
import com.springframework.section5.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class CustomerIntegrationTest {

	@Autowired
	CustomerController customerController;

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	private CustomerMapper customerMapper;

	@Test
	void testListCustomer() {
		List<CustomerDto> list = customerController.findAllCustomers();
		assertThat(list.size()).isEqualTo(3);
	}

	@Test
	void testEmptyList() {
		customerRepository.deleteAll();
		List<CustomerDto> list = customerController.findAllCustomers();
		assertThat(list).isEmpty();
	}

	@Test
	void testGetById() {
		Customer customer = customerRepository.findAll().get(0);
		CustomerDto expectedDto = customerMapper.customerToCustomerDto(customer);
		assertThat(expectedDto).isNotNull();
		CustomerDto actualDto = customerController.getCustomerById(customer.getId());
		assertThat(actualDto).isEqualTo(expectedDto);
	}

	@Test
	void testGetByIdAndNotFound() {
		assertThrows(NotFoundException.class, () ->
			customerController.getCustomerById(UUID.randomUUID()));
	}

	@Test
	void testSaveCustomer() {
		CustomerDto customerDto = CustomerDto.builder()
			.customerName("new name")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		ResponseEntity<String> response = customerController.handlePost(customerDto);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().getLocation()).isNotNull();

		String[] path = response.getHeaders().getLocation().getPath().split("/");
		UUID savedId = UUID.fromString(path[path.length - 1]);
		Optional<Customer> optional = customerRepository.findById(savedId);

		assertThat(optional).isNotEmpty();
		assertThat(optional.get().getCustomerName()).isEqualTo(customerDto.getCustomerName());
	}

	@Test
	void testUpdateCustomerById() {
		Customer customer = customerRepository.findAll().get(0);
		CustomerDto dto = customerMapper.customerToCustomerDto(customer);
		dto.setCustomerName("new");
		ResponseEntity<String> response = customerController.updateCustomerById(dto.getId(), dto);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Customer actualCustomer = customerRepository.findById(customer.getId()).get();
		assertThat(actualCustomer.getCustomerName()).isEqualTo("new");
	}

	@Test
	void testWhenBeerToUpdateIsNotFound() {
		assertThrows(NotFoundException.class, () ->
			customerController.updateCustomerById(UUID.randomUUID(), new CustomerDto()));
	}

	@Test
	void testPatchCustomerById() {
		Customer customer = customerRepository.findAll().get(0);
		CustomerDto dto = CustomerDto.builder()
			.id(customer.getId())
			.customerName("new")
			.build();
		ResponseEntity<String> response = customerController.updateCustomerById(dto.getId(), dto);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Customer actualCustomer = customerRepository.findById(customer.getId()).get();
		assertThat(actualCustomer.getCustomerName()).isEqualTo("new");
		assertThat(actualCustomer.getCreatedDate()).isEqualTo(customer.getCreatedDate());
	}

	@Test
	void testWhenBeerToPatchIsNotFound() {
		assertThrows(NotFoundException.class, () ->
			customerController.patchCustomerById(UUID.randomUUID(), new CustomerDto()));
	}

	@Test
	void testDeleteCustomerById() {
		Customer customer = customerRepository.findAll().get(0);
		ResponseEntity<String> response = customerController.deleteCustomerById(customer.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(customerRepository.findById(customer.getId())).isEmpty();
	}

	@Test
	void testWhenThereNoCustomerToDelete() {
		assertThrows(NotFoundException.class, () ->
			customerController.deleteCustomerById(UUID.randomUUID()));
	}
}
