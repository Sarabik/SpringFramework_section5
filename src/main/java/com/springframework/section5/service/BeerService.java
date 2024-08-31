package com.springframework.section5.service;

import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.entity.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BeerService {

	Page<BeerDto> listBeers(
		String beerName,
		BeerStyle beerStyle,
		Boolean showInventory,
		Integer pageNumber,
		Integer pageSize
	);

	BeerDto getBeerById(UUID id);

	BeerDto saveBeer(BeerDto beerDto);

	void updateBeerById(UUID id, BeerDto beerDto);

	void deleteBeerById(UUID id);

	void patchBeerById(UUID id, BeerDto beerDto);
}
