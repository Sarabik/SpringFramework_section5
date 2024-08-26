package com.springframework.section5.service;

import com.springframework.section5.dto.BeerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

	List<BeerDto> listBeers();

	Optional<BeerDto> getBeerById(UUID id);

	BeerDto saveBeer(BeerDto beerDto);

	void updateBeerById(UUID id, BeerDto beerDto);

	void deleteBeerById(UUID id);

	void patchBeerById(UUID id, BeerDto beerDto);
}
