package com.springframework.section5.service;

import com.springframework.section5.controller.NotFoundException;
import com.springframework.section5.dto.CustomerDto;
import com.springframework.section5.entity.Customer;
import com.springframework.section5.mapper.CustomerMapper;
import com.springframework.section5.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	private final CustomerMapper customerMapper;

	public CustomerServiceImpl(final CustomerRepository customerRepository, final CustomerMapper customerMapper) {
		this.customerRepository = customerRepository;
		this.customerMapper = customerMapper;
	}

	@Override
	public List<CustomerDto> findAllCustomers() {
		return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDto).toList();
	}

	@Override
	public CustomerDto getCustomerById(final UUID id) {
		Customer customer = getCustomer(id);
		return customerMapper.customerToCustomerDto(customer);
	}

	@Override
	public CustomerDto saveCustomer(final CustomerDto customerDto) {
		Customer saved = customerRepository.save(customerMapper.customerDtoToCustomer(customerDto));
		return customerMapper.customerToCustomerDto(saved);
	}

	@Override
	public void updateCustomerById(final UUID id, final CustomerDto customerDto) {
		Customer newCustomer = customerMapper.customerDtoToCustomer(customerDto);
		Customer existingCustomer = getCustomer(id);
		existingCustomer.setCustomerName(newCustomer.getCustomerName());
		existingCustomer.setCreatedDate(newCustomer.getCreatedDate());
		existingCustomer.setLastModifiedDate(newCustomer.getLastModifiedDate());
	}

	@Override
	public void deleteCustomerById(final UUID id) {
		if(!customerRepository.existsById(id)) {
			throw new NotFoundException();
		}
		customerRepository.deleteById(id);
	}

	@Override
	public void patchCustomerById(final UUID id, final CustomerDto customerDto) {
		Customer newCustomer = customerMapper.customerDtoToCustomer(customerDto);
		Customer existingCustomer = getCustomer(id);
		if (StringUtils.hasText(newCustomer.getCustomerName())) {
			existingCustomer.setCustomerName(newCustomer.getCustomerName());
		}
		if (newCustomer.getCreatedDate() != null) {
			existingCustomer.setCreatedDate(customerDto.getCreatedDate());
		}
		if (newCustomer.getLastModifiedDate() != null) {
			existingCustomer.setLastModifiedDate(customerDto.getLastModifiedDate());
		}
		customerRepository.save(existingCustomer);
	}

	private Customer getCustomer(UUID id) {
		return customerRepository.findById(id).orElseThrow(NotFoundException::new);
	}
}
