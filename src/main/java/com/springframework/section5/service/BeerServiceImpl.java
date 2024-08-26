package com.springframework.section5.service;

import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.dto.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

	private final Map<UUID, BeerDto> beerMap;

	public BeerServiceImpl() {
		this.beerMap = new HashMap<>();

		BeerDto beerDto1 = BeerDto.builder()
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

		BeerDto beerDto2 = BeerDto.builder()
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

		BeerDto beerDto3 = BeerDto.builder()
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

		beerMap.put(beerDto1.getId(), beerDto1);
		beerMap.put(beerDto2.getId(), beerDto2);
		beerMap.put(beerDto3.getId(), beerDto3);
	}

	@Override
	public List<BeerDto> listBeers() {
		log.debug("Using listBeers method - in BeerServiceImpl");
		return new ArrayList<>(beerMap.values());
	}

	@Override
	public Optional<BeerDto> getBeerById(final UUID id) {
		log.debug("Using getBeerById method - in BeerServiceImpl");
		return Optional.of(beerMap.get(id));
	}

	@Override
	public BeerDto saveBeer(final BeerDto beerDto) {
		UUID id = UUID.randomUUID();
		beerDto.setId(id);
		beerMap.putIfAbsent(id, beerDto);
		return beerDto;
	}

	@Override
	public void updateBeerById(final UUID id, final BeerDto beerDto) {
		BeerDto existingBeerDto = beerMap.get(id);
		existingBeerDto.setBeerName(beerDto.getBeerName());
		existingBeerDto.setBeerStyle(beerDto.getBeerStyle());
		existingBeerDto.setPrice(beerDto.getPrice());
		existingBeerDto.setUpc(beerDto.getUpc());
		existingBeerDto.setCreatedDate(beerDto.getCreatedDate());
		existingBeerDto.setUpdateDate(beerDto.getUpdateDate());
		existingBeerDto.setVersion(beerDto.getVersion());
		existingBeerDto.setQuantityOnHand(beerDto.getQuantityOnHand());
	}

	@Override
	public void deleteBeerById(final UUID id) {
		beerMap.remove(id);
	}

	@Override
	public void patchBeerById(final UUID id, final BeerDto beerDto) {
		BeerDto existingBeerDto = beerMap.get(id);

		if (StringUtils.hasText(beerDto.getBeerName())) {
			existingBeerDto.setBeerName(beerDto.getBeerName());
		}
		if (StringUtils.hasText(beerDto.getUpc())) {
			existingBeerDto.setUpc(beerDto.getUpc());
		}
		if (beerDto.getBeerStyle() != null) {
			existingBeerDto.setBeerStyle(beerDto.getBeerStyle());
		}
		if (beerDto.getCreatedDate() != null) {
			existingBeerDto.setUpdateDate(beerDto.getUpdateDate());
		}
		if (beerDto.getUpdateDate() != null) {
			existingBeerDto.setUpdateDate(beerDto.getUpdateDate());
		}
		if (beerDto.getPrice() != null) {
			existingBeerDto.setPrice(beerDto.getPrice());
		}
		if (beerDto.getQuantityOnHand() != null) {
			existingBeerDto.setQuantityOnHand(beerDto.getQuantityOnHand());
		}
		if (beerDto.getVersion() != null) {
			existingBeerDto.setVersion(beerDto.getVersion());
		}
	}
}
