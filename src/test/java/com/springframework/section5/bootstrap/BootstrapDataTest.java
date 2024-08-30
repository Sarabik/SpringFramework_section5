package com.springframework.section5.bootstrap;

import com.springframework.section5.repository.BeerRepository;
import com.springframework.section5.repository.CustomerRepository;
import com.springframework.section5.service.BeerCsvService;
import com.springframework.section5.service.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	BeerCsvService beerCsvService;

	BootstrapData bootstrapData;

	@BeforeEach
	void setUp() {
		bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
	}

	@Test
	void testRun() throws Exception {

		bootstrapData.run(null);

		assertThat(beerRepository.count()).isGreaterThan(0);
		assertThat(customerRepository.count()).isGreaterThan(0);

	}
}