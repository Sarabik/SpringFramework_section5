package com.springframework.section5.controller;

import com.springframework.section5.model.Beer;
import com.springframework.section5.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BeerController {

	private static final Logger log = LoggerFactory.getLogger(BeerController.class);
	private final BeerService beerService;

	public Beer getBeerById(UUID id) {
		log.debug("Using BeerController getBeerById method");
		return beerService.getBeerById(id);
	}
}
