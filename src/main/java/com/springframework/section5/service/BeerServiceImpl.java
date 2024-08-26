package com.springframework.section5.service;

import com.springframework.section5.controller.NotFoundException;
import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.entity.Beer;
import com.springframework.section5.mapper.BeerMapper;
import com.springframework.section5.repository.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

	private final BeerMapper beerMapper;

	private final BeerRepository beerRepository;

	public BeerServiceImpl(
		final BeerRepository beerRepository,
		final BeerMapper beerMapper
	) {
		this.beerRepository = beerRepository;
		this.beerMapper = beerMapper;
	}

	@Override
	public List<BeerDto> listBeers() {
		log.debug("Using listBeers method - in BeerServiceImpl");
		return beerRepository.findAll().stream().map(beerMapper::beerToBeerDto).toList();
	}

	@Override
	public BeerDto getBeerById(final UUID id) {
		log.debug("Using getBeerById method - in BeerServiceImpl");
		Beer beer = getBeer(id);
		return beerMapper.beerToBeerDto(beer);
	}

	@Override
	public BeerDto saveBeer(final BeerDto beerDto) {
		Beer saved = beerRepository.save(beerMapper.beerDtoToBeer(beerDto));
		return beerMapper.beerToBeerDto(saved);
	}

	@Override
	public void updateBeerById(final UUID id, final BeerDto beerDto) {
		Beer newBeer = beerMapper.beerDtoToBeer(beerDto);
		Beer existingBeer = getBeer(id);
		existingBeer.setBeerName(newBeer.getBeerName());
		existingBeer.setBeerStyle(newBeer.getBeerStyle());
		existingBeer.setPrice(newBeer.getPrice());
		existingBeer.setUpc(newBeer.getUpc());
		existingBeer.setCreatedDate(newBeer.getCreatedDate());
		existingBeer.setUpdateDate(newBeer.getUpdateDate());
		existingBeer.setQuantityOnHand(newBeer.getQuantityOnHand());
		beerRepository.save(existingBeer);
	}

	@Override
	public void deleteBeerById(final UUID id) {
		if (!beerRepository.existsById(id)) {
			throw new NotFoundException();
		}
		beerRepository.deleteById(id);
	}

	@Override
	public void patchBeerById(final UUID id, final BeerDto beerDto) {
		Beer newBeer = beerMapper.beerDtoToBeer(beerDto);
		Beer existingBeer = getBeer(id);

		if (StringUtils.hasText(newBeer.getBeerName())) {
			existingBeer.setBeerName(newBeer.getBeerName());
		}
		if (StringUtils.hasText(newBeer.getUpc())) {
			existingBeer.setUpc(newBeer.getUpc());
		}
		if (newBeer.getBeerStyle() != null) {
			existingBeer.setBeerStyle(newBeer.getBeerStyle());
		}
		if (newBeer.getCreatedDate() != null) {
			existingBeer.setUpdateDate(newBeer.getUpdateDate());
		}
		if (newBeer.getUpdateDate() != null) {
			existingBeer.setUpdateDate(newBeer.getUpdateDate());
		}
		if (newBeer.getPrice() != null) {
			existingBeer.setPrice(newBeer.getPrice());
		}
		if (newBeer.getQuantityOnHand() != null) {
			existingBeer.setQuantityOnHand(newBeer.getQuantityOnHand());
		}
		if (newBeer.getVersion() != null) {
			existingBeer.setVersion(newBeer.getVersion());
		}
		beerRepository.save(existingBeer);
	}

	private Beer getBeer(UUID id) {
		return beerRepository.findById(id).orElseThrow(NotFoundException::new);
	}
}
