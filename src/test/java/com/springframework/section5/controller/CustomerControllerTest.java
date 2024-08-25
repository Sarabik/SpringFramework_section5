package com.springframework.section5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.section5.model.Customer;
import com.springframework.section5.service.CustomerService;
import com.springframework.section5.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.springframework.section5.controller.CustomerController.CUSTOMER_PATH;
import static com.springframework.section5.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

	List<Customer> list;
	Customer customer;

	@BeforeEach
	void setUp() {
		list = new CustomerServiceImpl().findAllCustomers();
		customer = list.get(0);
	}

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CustomerService customerService;

	@Autowired
	ObjectMapper objectMapper;

	@Captor
	ArgumentCaptor<UUID> captorUUID = ArgumentCaptor.forClass(UUID.class);

	@Captor
	ArgumentCaptor<Customer> captorCustomer = ArgumentCaptor.forClass(Customer.class);

	@Test
	void findAllCustomers() throws Exception {
		when(customerService.findAllCustomers()).thenReturn(list);

		mockMvc.perform(
			get(CUSTOMER_PATH)
				.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)));
	}

	@Test
	void getCustomerById() throws Exception {
		when(customerService.getCustomerById(customer.getId())).thenReturn(customer);

		mockMvc.perform(
			get(CUSTOMER_PATH_ID, customer.getId())
				.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(customer.getId().toString())));
	}

	@Test
	void handlePost() throws Exception {
		when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);

		mockMvc.perform(
			post(CUSTOMER_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))
		)
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void updateCustomerById() throws Exception {
		mockMvc.perform(
			put(CUSTOMER_PATH_ID, customer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customer))
		)
			.andExpect(status().isNoContent());

		verify(customerService, times(1))
						.updateCustomerById(any(UUID.class), any(Customer.class));
	}

	@Test
	void deleteCustomerById() throws Exception {
		mockMvc.perform(
			delete(CUSTOMER_PATH_ID, customer.getId())
				.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isNoContent());

		verify(customerService, times(1)).deleteCustomerById(captorUUID.capture());
		assertThat(customer.getId()).isEqualTo(captorUUID.getValue());
	}

	@Test
	void patchCustomerById() throws Exception {

		Customer newCustomer = new Customer();
		newCustomer.setId(customer.getId());
		newCustomer.setCustomerName("new name");

		mockMvc.perform(
			patch(CUSTOMER_PATH_ID, customer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newCustomer))
		)
			.andExpect(status().isNoContent());

		verify(customerService, times(1))
								.patchCustomerById(captorUUID.capture(), captorCustomer.capture());
		assertThat(newCustomer.getId()).isEqualTo(captorUUID.getValue());
		assertThat(newCustomer.getCustomerName()).isEqualTo(captorCustomer.getValue().getCustomerName());
	}
}