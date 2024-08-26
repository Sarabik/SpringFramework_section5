package com.springframework.section5.repository;

import com.springframework.section5.entity.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class CustomerRepositoryTest {

	static Customer customer1;
	static Customer customer2;

	@BeforeAll
	static void init() {
		customer1 = Customer.builder().customerName("name1").build();
		customer2 = Customer.builder().customerName("name2").build();
	}
	
	@Autowired
	CustomerRepository customerRepository;

	@Test
	void testSaveCustomer() {
		Customer savedCustomer = customerRepository.save(customer1);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isNotNull();
		assertThat(savedCustomer.getCustomerName()).isEqualTo("name1");
	}

	@Test
	void testFindCustomerById() {
		Customer savedCustomer = customerRepository.save(customer1);

		Customer customer = customerRepository.findById(savedCustomer.getId()).get();

		assertThat(customer).isEqualTo(savedCustomer);
	}

	@Test
	void testFindAllCustomer() {
		Customer saved1 = customerRepository.save(customer1);
		Customer saved2 = customerRepository.save(customer2);
		List<Customer> listExpected = List.of(saved1, saved2);

		List<Customer> list = customerRepository.findAll();

		assertThat(list.size()).isEqualTo(2);
		assertThat(list).containsAll(listExpected);
	}

	@Test
	void testDeleteCustomer() {
		Customer saved1 = customerRepository.save(customer1);
		assertThat(customerRepository.count()).isEqualTo(1);

		customerRepository.deleteById(saved1.getId());

		assertThat(customerRepository.count()).isEqualTo(0);
	}
}