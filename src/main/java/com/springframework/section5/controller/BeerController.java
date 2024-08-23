package com.springframework.section5.controller;

import com.springframework.section5.model.Beer;
import com.springframework.section5.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/beer")
public class BeerController {

	private final BeerService beerService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Beer> listBeers() {
		log.debug("Using listBeers method - in BeerController");
		return beerService.listBeers();
	}

	@RequestMapping(value = "{beerId}", method = RequestMethod.GET)
	public Beer getBeerById(@PathVariable("beerId") UUID id) {
		log.debug("Using getBeerById method - in BeerController");
		return beerService.getBeerById(id);
	}
}
