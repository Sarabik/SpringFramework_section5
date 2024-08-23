package com.springframework.section5.service;

import com.springframework.section5.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

	List<Beer> listBeers();

	Beer getBeerById(UUID id);

	Beer saveBeer(Beer beer);

	void updateBeerById(UUID id, Beer beer);

	void deleteBeerById(UUID id);

	void patchBeerById(UUID id, Beer beer);
}
