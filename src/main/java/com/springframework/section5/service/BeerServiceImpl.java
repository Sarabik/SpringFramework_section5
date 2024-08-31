package com.springframework.section5.service;

import com.springframework.section5.controller.NotFoundException;
import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.entity.Beer;
import com.springframework.section5.mapper.BeerMapper;
import com.springframework.section5.repository.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

	private final BeerMapper beerMapper;

	private final BeerRepository beerRepository;

	private static final int DEFAULT_PAGE_NUMBER = 0;
	private static final int MAX_PAGE_NUMBER = 100;
	private static final int DEFAULT_PAGE_SIZE = 25;
	private static final int MAX_PAGE_SIZE = 100;

	public BeerServiceImpl(
		final BeerRepository beerRepository,
		final BeerMapper beerMapper
	) {
		this.beerRepository = beerRepository;
		this.beerMapper = beerMapper;
	}

	@Override
	public Page<BeerDto> listBeers(
		String beerName,
		BeerStyle beerStyle,
		Boolean showInventory,
		Integer pageNumber,
		Integer pageSize
	) {
		log.debug("Using listBeers method - in BeerServiceImpl");

		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

		Page<Beer> beerPage;
		if(StringUtils.hasText(beerName) && beerStyle == null) {
			beerPage = listBeersByName(beerName, pageRequest);
		} else if(!StringUtils.hasText(beerName) && beerStyle != null) {
			beerPage = listBeersByStyle(beerStyle, pageRequest);
		} else if(StringUtils.hasText(beerName) && beerStyle != null) {
			beerPage = listBeersByNameAndStyle(beerName, beerStyle, pageRequest);
		} else {
			beerPage = beerRepository.findAll(pageRequest);
		}
		if (showInventory != null && !showInventory) {
			beerPage.forEach(beer -> beer.setQuantityOnHand(null));
		}
		return beerPage.map(beerMapper::beerToBeerDto);
	}

	private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
		int queryPageNumber;
		int queryPageSize;

		if (pageNumber == null || pageNumber <= 0) {
			queryPageNumber = DEFAULT_PAGE_NUMBER;
		} else if (pageNumber > MAX_PAGE_NUMBER) {
			queryPageNumber = MAX_PAGE_SIZE;
		} else {
			queryPageNumber = pageNumber - 1;
		}

		if (pageSize == null || pageSize <= 0) {
			queryPageSize = DEFAULT_PAGE_SIZE;
		} else if (pageSize > MAX_PAGE_SIZE) {
			queryPageSize = MAX_PAGE_SIZE;
		} else {
			queryPageSize = pageSize;
		}

		Sort sort = Sort.by(Sort.Order.asc("beerName"));

		return PageRequest.of(queryPageNumber, queryPageSize, sort);
	}

	private Page<Beer> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
		return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageable);
	}

	private Page<Beer> listBeersByName(String beerName, Pageable pageable) {
		return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
	}

	private Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
		return beerRepository.findAllByBeerStyle(beerStyle, pageable);
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
