package com.springframework.section5.repository;

import com.springframework.section5.entity.Beer;
import com.springframework.section5.entity.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
	List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);
	List<Beer> findAllByBeerStyle(BeerStyle beerStyle);
	List<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle);
}
