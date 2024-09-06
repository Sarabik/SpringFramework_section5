package com.springframework.section5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.section5.config.SpringSecurityConfig;
import com.springframework.section5.dto.CustomerDto;
import com.springframework.section5.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.springframework.section5.controller.CustomerController.CUSTOMER_PATH;
import static com.springframework.section5.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
@WithMockUser(username = "user1", password = "password")
@Import(SpringSecurityConfig.class)
class CustomerControllerTest {

	List<CustomerDto> list;
	CustomerDto customerDto;

	@BeforeEach
	void setUp() {
		CustomerDto customerDto1 = CustomerDto.builder()
			.id(UUID.randomUUID())
			.customerName("name1")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		CustomerDto customerDto2 = CustomerDto.builder()
			.id(UUID.randomUUID())
			.customerName("name2")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		CustomerDto customerDto3 = CustomerDto.builder()
			.id(UUID.randomUUID())
			.customerName("name3")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.build();

		list = List.of(customerDto1, customerDto2, customerDto3);
		customerDto = customerDto1;
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
	ArgumentCaptor<CustomerDto> captorCustomer = ArgumentCaptor.forClass(CustomerDto.class);

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
		when(customerService.getCustomerById(customerDto.getId())).thenReturn(customerDto);

		mockMvc.perform(
			get(CUSTOMER_PATH_ID, customerDto.getId())
				.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(customerDto.getId().toString())));
	}

	@Test
	void customerByIdNotFound() throws Exception {
		when(customerService.getCustomerById(any(UUID.class))).thenThrow(new NotFoundException());

		mockMvc.perform(get(CUSTOMER_PATH_ID, UUID.randomUUID()))
			.andExpect(status().isNotFound())
			.andExpect(result ->
				assertInstanceOf(NotFoundException.class, result.getResolvedException()));
	}

	@Test
	void handlePost() throws Exception {
		when(customerService.saveCustomer(any(CustomerDto.class))).thenReturn(customerDto);

		mockMvc.perform(
			post(CUSTOMER_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDto))
		)
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void updateCustomerById() throws Exception {
		mockMvc.perform(
			put(CUSTOMER_PATH_ID, customerDto.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerDto))
		)
			.andExpect(status().isNoContent());

		verify(customerService, times(1))
						.updateCustomerById(any(UUID.class), any(CustomerDto.class));
	}

	@Test
	void deleteCustomerById() throws Exception {
		mockMvc.perform(
			delete(CUSTOMER_PATH_ID, customerDto.getId())
				.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isNoContent());

		verify(customerService, times(1)).deleteCustomerById(captorUUID.capture());
		assertThat(customerDto.getId()).isEqualTo(captorUUID.getValue());
	}

	@Test
	void patchCustomerById() throws Exception {

		CustomerDto newCustomerDto = new CustomerDto();
		newCustomerDto.setId(customerDto.getId());
		newCustomerDto.setCustomerName("new name");

		mockMvc.perform(
			patch(CUSTOMER_PATH_ID, customerDto.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newCustomerDto))
		)
			.andExpect(status().isNoContent());

		verify(customerService, times(1))
								.patchCustomerById(captorUUID.capture(), captorCustomer.capture());
		assertThat(newCustomerDto.getId()).isEqualTo(captorUUID.getValue());
		assertThat(newCustomerDto.getCustomerName()).isEqualTo(captorCustomer.getValue().getCustomerName());
	}
}