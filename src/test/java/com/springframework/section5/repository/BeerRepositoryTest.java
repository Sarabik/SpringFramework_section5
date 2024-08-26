package com.springframework.section5.repository;

import com.springframework.section5.entity.Beer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
class BeerRepositoryTest {

	static Beer beer1;
	static Beer beer2;

	@BeforeAll
	static void init() {
		beer1 = Beer.builder().beerName("name1").build();
		beer2 = Beer.builder().beerName("name2").build();
	}

	@Autowired
	BeerRepository beerRepository;

	@Test
	void testSaveBeer() {
		Beer savedBeer = beerRepository.save(beer1);

		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
		assertThat(savedBeer.getBeerName()).isEqualTo("name1");
	}

	@Test
	void testFindBeerById() {
		Beer savedBeer = beerRepository.save(beer1);

		Beer beer = beerRepository.findById(savedBeer.getId()).get();

		assertThat(beer).isEqualTo(savedBeer);
	}

	@Test
	void testFindAllBeer() {
		Beer saved1 = beerRepository.save(beer1);
		Beer saved2 = beerRepository.save(beer2);
		List<Beer> listExpected = List.of(saved1, saved2);

		List<Beer> list = beerRepository.findAll();

		assertThat(list.size()).isEqualTo(2);
		assertThat(list).containsAll(listExpected);
	}

	@Test
	void testDeleteBeer() {
		Beer saved1 = beerRepository.save(beer1);
		assertThat(beerRepository.count()).isEqualTo(1);

		beerRepository.deleteById(saved1.getId());

		assertThat(beerRepository.count()).isEqualTo(0);
	}

}