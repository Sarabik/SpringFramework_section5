package com.springframework.section5.controller;

import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BeerController {

	public static final String BEER_PATH = "/api/v1/beer";
	public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

	private final BeerService beerService;

	@PostMapping(BEER_PATH)
	public ResponseEntity<String> handlePost(@RequestBody BeerDto beerDto) {
		BeerDto savedBeerDto = beerService.saveBeer(beerDto);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/beerDto/" + savedBeerDto.getId().toString());

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@GetMapping(BEER_PATH)
	public ResponseEntity<List<BeerDto>> listBeers() {
		log.debug("Using listBeers method - in BeerController");
		return new ResponseEntity<>(beerService.listBeers(), HttpStatus.OK);
	}

	@GetMapping(BEER_PATH_ID)
	public BeerDto getBeerById(@PathVariable("beerId") UUID id) {
		log.debug("Using getBeerById method - in BeerController");
		return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
	}

	@PutMapping(BEER_PATH_ID)
	public ResponseEntity<String> updateBeerById(
					@PathVariable("beerId") UUID id,
					@RequestBody BeerDto beerDto
	) {
		beerService.updateBeerById(id, beerDto);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(BEER_PATH_ID)
	public ResponseEntity<String> deleteBeerById(@PathVariable("beerId") UUID id) {
		beerService.deleteBeerById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping(BEER_PATH_ID)
	public ResponseEntity<String> patchBeerById(
		@PathVariable("beerId") UUID id,
		@RequestBody BeerDto beerDto
	) {
		beerService.patchBeerById(id, beerDto);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
