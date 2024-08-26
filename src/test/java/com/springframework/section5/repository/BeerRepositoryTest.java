package com.springframework.section5.repository;

import com.springframework.section5.entity.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

	@Autowired
	BeerRepository beerRepository;

	@Test
	void testSaveBeer() {
		String name = "beer name";
		Beer savedBeer = beerRepository.save(Beer.builder().beerName(name).build());
		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
		assertThat(savedBeer.getBeerName()).isEqualTo(name);
	}

}