package com.springframework.section5.repository;

import com.springframework.section5.entity.Beer;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	CategoryRepository categoryRepository;

	Category category1;
	Beer savedBeer;

	@BeforeEach
	void setUp() {
		category1 = new Category();
		category1.setDescription("description1");
		Beer beer = Beer.builder()
			.beerName("name1")
			.beerStyle(BeerStyle.LAGER)
			.upc("ttt")
			.price(BigDecimal.valueOf(1))
			.build();
		this.savedBeer = beerRepository.save(beer);
	}

	@Test
	@Transactional
	@Order(1)
	void testAddCategory() {
		category1.addBeer(savedBeer);
		Category savedCategory = categoryRepository.save(category1);
		categoryRepository.flush();
		assertThat(savedCategory.getId()).isEqualTo(savedBeer.getCategories().stream().findFirst().get().getId());
		System.out.println(savedCategory.getBeers());
	}

	@Test
	@Transactional
	@Order(2)
	void testRemoveCategory() {
		category1.addBeer(savedBeer);
		categoryRepository.save(category1);
		categoryRepository.flush();

		Category category = categoryRepository.findAll().get(0);
		Beer beer = category.getBeers().stream().findFirst().get();
		category.removeBeer(beer);
		Category savedCategory = categoryRepository.save(category);
		categoryRepository.flush();

		assertThat(beerRepository.findById(beer.getId()).get().getCategories()).isEmpty();
		assertThat(savedCategory.getBeers()).isEmpty();

	}
}