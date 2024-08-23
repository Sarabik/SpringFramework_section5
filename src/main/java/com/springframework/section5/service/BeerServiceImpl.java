package com.springframework.section5.service;

import com.springframework.section5.model.Beer;
import com.springframework.section5.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

	private final Map<UUID, Beer> beerMap;

	public BeerServiceImpl() {
		this.beerMap = new HashMap<>();

		Beer beer1 = Beer.builder()
			.id(UUID.randomUUID())
			.version(1)
			.beerName("Galaxy Cat")
			.beerStyle(BeerStyle.PALE_ALE)
			.upc("12356")
			.price(new BigDecimal("12.99"))
			.quantityOnHand(122)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		Beer beer2 = Beer.builder()
			.id(UUID.randomUUID())
			.version(1)
			.beerName("Crank")
			.beerStyle(BeerStyle.PALE_ALE)
			.upc("12356222")
			.price(new BigDecimal("11.99"))
			.quantityOnHand(392)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		Beer beer3 = Beer.builder()
			.id(UUID.randomUUID())
			.version(1)
			.beerName("Sunshine City")
			.beerStyle(BeerStyle.IPA)
			.upc("12356")
			.price(new BigDecimal("13.99"))
			.quantityOnHand(144)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		beerMap.put(beer1.getId(), beer1);
		beerMap.put(beer2.getId(), beer2);
		beerMap.put(beer3.getId(), beer3);
	}

	@Override
	public List<Beer> listBeers() {
		log.debug("Using listBeers method - in BeerServiceImpl");
		return new ArrayList<>(beerMap.values());
	}

	@Override
	public Beer getBeerById(final UUID id) {
		log.debug("Using getBeerById method - in BeerServiceImpl");
		return beerMap.get(id);
	}

	@Override
	public Beer saveBeer(final Beer beer) {
		UUID id = UUID.randomUUID();
		beer.setId(id);
		beerMap.putIfAbsent(id, beer);
		return beer;
	}

	@Override
	public void updateBeerById(final UUID id, final Beer beer) {
		Beer existingBeer = beerMap.get(id);
		existingBeer.setBeerName(beer.getBeerName());
		existingBeer.setBeerStyle(beer.getBeerStyle());
		existingBeer.setPrice(beer.getPrice());
		existingBeer.setUpc(beer.getUpc());
		existingBeer.setCreatedDate(beer.getCreatedDate());
		existingBeer.setUpdateDate(beer.getUpdateDate());
		existingBeer.setVersion(beer.getVersion());
		existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
	}

	@Override
	public void deleteBeerById(final UUID id) {
		beerMap.remove(id);
	}

	@Override
	public void patchBeerById(final UUID id, final Beer beer) {
		Beer existingBeer = beerMap.get(id);

		if (StringUtils.hasText(beer.getBeerName())) {
			existingBeer.setBeerName(beer.getBeerName());
		}
		if (StringUtils.hasText(beer.getUpc())) {
			existingBeer.setUpc(beer.getUpc());
		}
		if (beer.getBeerStyle() != null) {
			existingBeer.setBeerStyle(beer.getBeerStyle());
		}
		if (beer.getCreatedDate() != null) {
			existingBeer.setUpdateDate(beer.getUpdateDate());
		}
		if (beer.getUpdateDate() != null) {
			existingBeer.setUpdateDate(beer.getUpdateDate());
		}
		if (beer.getPrice() != null) {
			existingBeer.setPrice(beer.getPrice());
		}
		if (beer.getQuantityOnHand() != null) {
			existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
		}
		if (beer.getVersion() != null) {
			existingBeer.setVersion(beer.getVersion());
		}
	}
}
