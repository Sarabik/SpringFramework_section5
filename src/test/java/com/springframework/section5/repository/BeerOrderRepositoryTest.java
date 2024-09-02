package com.springframework.section5.repository;

import com.springframework.section5.entity.Beer;
import com.springframework.section5.entity.BeerOrder;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerOrderRepositoryTest {

	@Autowired
	BeerOrderRepository beerOrderRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	BeerRepository beerRepository;

	Customer customer;
	Beer beer;

	@BeforeEach
	void setUp() {
		Beer beer = Beer.builder()
			.beerName("Galaxy Cat")
			.beerStyle(BeerStyle.PALE_ALE)
			.upc("12356")
			.price(new BigDecimal("12.99"))
			.quantityOnHand(122)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();
		Customer customer = Customer.builder()
			.customerName("name1")
			.createdDate(LocalDateTime.now())
			.lastModifiedDate(LocalDateTime.now())
			.email("email@mail.com")
			.beerOrders(new HashSet<>())
			.build();
		this.beer = beerRepository.save(beer);
		this.customer = customerRepository.save(customer);
	}

	@Test
	@Transactional
	void testBeerOrder() {
		BeerOrder beerOrder = new BeerOrder();
		beerOrder.setCustomerRef("customer reference");
		beerOrder.setCustomer(customer);

		BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
		beerRepository.flush();
		assertThat(savedBeerOrder.getId()).isNotNull();
		UUID expectedBeerOrderId = customerRepository.findById(customer.getId()).get()
			.getBeerOrders().stream().findFirst().get().getId();
		assertThat(expectedBeerOrderId).isEqualTo(savedBeerOrder.getId());
	}
}