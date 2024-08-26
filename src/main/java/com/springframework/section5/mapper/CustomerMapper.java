package com.springframework.section5.mapper;

import com.springframework.section5.dto.CustomerDto;
import com.springframework.section5.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

	Customer customerDtoToCustomer(CustomerDto dto);
	CustomerDto customerToCustomerDto(Customer entity);

}
