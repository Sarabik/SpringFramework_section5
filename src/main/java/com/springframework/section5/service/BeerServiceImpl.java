package com.springframework.section5.service;

import com.springframework.section5.model.Beer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {
	@Override
	public Beer getBeerById(final UUID id) {

		log.debug("Get Beer Id in service was called");

		return Beer.builder()
			.id(id)
			.version(1)
			.beerName("name")
			.upc("123")
			.price(new BigDecimal("12.99"))
			.quantityOnHand(122)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();
	}
}
