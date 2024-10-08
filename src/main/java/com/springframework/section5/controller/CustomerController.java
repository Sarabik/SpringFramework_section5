package com.springframework.section5.controller;

import com.springframework.section5.dto.CustomerDto;
import com.springframework.section5.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CustomerController {

	public static final String CUSTOMER_PATH = "/api/v1/customer";
	public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

	private final CustomerService customerService;

	@GetMapping(CUSTOMER_PATH)
	public List<CustomerDto> findAllCustomers() {
		return customerService.findAllCustomers();
	}

	@GetMapping(CUSTOMER_PATH_ID)
	public CustomerDto getCustomerById(@PathVariable("customerId") final UUID id) {
		return customerService.getCustomerById(id);
	}

	@PostMapping(CUSTOMER_PATH)
	public ResponseEntity<String> handlePost(@RequestBody CustomerDto customerDto) {
		CustomerDto savedCustomerDto = customerService.saveCustomer(customerDto);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/customerDto/" + savedCustomerDto.getId().toString());

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@PutMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<String> updateCustomerById(
					@PathVariable("customerId") UUID id,
					@RequestBody CustomerDto customerDto
	) {
		customerService.updateCustomerById(id, customerDto);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<String> deleteCustomerById(@PathVariable("customerId") UUID id) {
		customerService.deleteCustomerById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping(CUSTOMER_PATH_ID)
	public ResponseEntity<String> patchCustomerById(
		@PathVariable("customerId") UUID id,
		@RequestBody CustomerDto customerDto
	) {
		customerService.patchCustomerById(id, customerDto);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
