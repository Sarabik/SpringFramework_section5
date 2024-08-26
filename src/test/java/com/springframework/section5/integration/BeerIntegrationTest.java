package com.springframework.section5.integration;

import com.springframework.section5.controller.BeerController;
import com.springframework.section5.controller.NotFoundException;
import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.entity.Beer;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.mapper.BeerMapper;
import com.springframework.section5.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class BeerIntegrationTest {

	@Autowired
	BeerController beerController;

	@Autowired
	BeerRepository beerRepository;
	@Autowired
	private BeerMapper beerMapper;

	@Test
	void testListBeer() {
		List<BeerDto> list = beerController.listBeers();
		assertThat(list.size()).isEqualTo(3);
	}

	@Test
	void testEmptyList() {
		beerRepository.deleteAll();
		List<BeerDto> list = beerController.listBeers();
		assertThat(list).isEmpty();
	}

	@Test
	void testGetById() {
		Beer beer = beerRepository.findAll().get(0);
		BeerDto expectedDto = beerMapper.beerToBeerDto(beer);
		assertThat(expectedDto).isNotNull();
		BeerDto actualDto = beerController.getBeerById(beer.getId());
		assertThat(actualDto).isEqualTo(expectedDto);
	}

	@Test
	void testGetByIdAndNotFound() {
		assertThrows(NotFoundException.class, () ->
			beerController.getBeerById(UUID.randomUUID()));
	}

	@Test
	void testSaveBeer() {
		BeerDto beerDto = BeerDto.builder()
			.beerName("Galaxy")
			.beerStyle(BeerStyle.PALE_ALE)
			.upc("12356")
			.price(new BigDecimal("40.99"))
			.quantityOnHand(30)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		ResponseEntity<String> response = beerController.handlePost(beerDto);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getHeaders().getLocation()).isNotNull();

		String[] path = response.getHeaders().getLocation().getPath().split("/");
		UUID savedId = UUID.fromString(path[path.length - 1]);
		Optional<Beer> optional = beerRepository.findById(savedId);

		assertThat(optional).isNotEmpty();
		assertThat(optional.get().getBeerName()).isEqualTo(beerDto.getBeerName());
	}

	@Test
	void testUpdateBeerById() {
		Beer beer = beerRepository.findAll().get(0);
		BeerDto dto = beerMapper.beerToBeerDto(beer);
		dto.setBeerName("new");
		ResponseEntity<String> response = beerController.updateBeerById(beer.getId(), dto);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Beer actualBeer = beerRepository.findById(beer.getId()).get();
		assertThat(actualBeer.getBeerName()).isEqualTo("new");
	}

	@Test
	void testWhenBeerToUpdateIsNotFound() {
		assertThrows(NotFoundException.class, () ->
			beerController.updateBeerById(UUID.randomUUID(), new BeerDto()));
	}

	@Test
	void testPatchBeerById() {
		Beer beer = beerRepository.findAll().get(0);
		BeerDto dto = BeerDto.builder()
			.id(beer.getId())
			.beerName("new")
			.build();
		ResponseEntity<String> response = beerController.updateBeerById(dto.getId(), dto);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		Beer actualBeer = beerRepository.findById(beer.getId()).get();
		assertThat(actualBeer.getBeerName()).isEqualTo("new");
		assertThat(actualBeer.getPrice()).isEqualTo(beer.getPrice());
	}

	@Test
	void testWhenBeerToPatchIsNotFound() {
		assertThrows(NotFoundException.class, () ->
			beerController.patchBeerById(UUID.randomUUID(), new BeerDto()));
	}

	@Test
	void testDeleteBeerById() {
		Beer beer = beerRepository.findAll().get(0);
		ResponseEntity<String> response = beerController.deleteBeerById(beer.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(beerRepository.findById(beer.getId())).isEmpty();
	}

	@Test
	void testWhenThereNoBeerToDelete() {
		assertThrows(NotFoundException.class, () ->
			beerController.deleteBeerById(UUID.randomUUID()));
	}
}
