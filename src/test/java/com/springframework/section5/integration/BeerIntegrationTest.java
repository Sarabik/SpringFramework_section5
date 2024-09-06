package com.springframework.section5.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.section5.config.SpringSecurityConfig;
import com.springframework.section5.controller.BeerController;
import com.springframework.section5.controller.NotFoundException;
import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.entity.Beer;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.mapper.BeerMapper;
import com.springframework.section5.repository.BeerRepository;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.springframework.section5.controller.BeerController.BEER_PATH;
import static com.springframework.section5.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerIntegrationTest {

	@Autowired
	BeerController beerController;

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	BeerMapper beerMapper;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
			.apply(springSecurity())
			.build();
	}

	@Test
	void testListBeer() {
		Page<BeerDto> page = beerController.listBeers(null, null, false, 1, 25);
		assertThat(page.get().count()).isGreaterThan(0);
	}

	@Test
	@WithMockUser(username = "user1", password = "password")
	void whenGetListOfBeersByName() throws Exception {
		mockMvc.perform(
				get(BEER_PATH)
					.queryParam("beerName", "Nonstop Hef Hop")
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements", is(12)));
	}

	@Test
	void whenGetListOfBeersWithoutAuth() throws Exception {
		mockMvc.perform(
				get(BEER_PATH)
			)
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "user1", password = "password")
	void whenGetListOfBeersByStyle() throws Exception {
		mockMvc.perform(
				get(BEER_PATH)
					.queryParam("beerStyle", BeerStyle.PILSNER.name())
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements", is(1160)));
	}

	@Test
	@WithMockUser(username = "user1", password = "password")
	void tesListBeersByStyleAndNameShowInventoryTrue() throws Exception {

		mockMvc.perform(get(BEER_PATH)
				.queryParam("beerName", "Nonstop Hef Hop")
				.queryParam("beerStyle", BeerStyle.PILSNER.name())
				.queryParam("showInventory", "true"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements", is(12)))
			.andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
	}

	@Test
	@WithMockUser(username = "user1", password = "password")
	void tesListBeersByStyleAndNameShowInventoryFalse() throws Exception {
		mockMvc.perform(get(BEER_PATH)
				.queryParam("beerName", "Nonstop Hef Hop")
				.queryParam("beerStyle", BeerStyle.PILSNER.name())
				.queryParam("showInventory", "false"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements", is(12)))
			.andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
	}

	@Test
	@WithMockUser(username = "user1", password = "password")
	void testListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {
		mockMvc.perform(get(BEER_PATH)
			.queryParam("pageNumber", "2")
			.queryParam("pageSize", "25")
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size", is(25)))
			.andExpect(jsonPath("$.totalElements", is(2410)))
			.andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
	}

	@Test
	@Transactional
	void testEmptyList() {
		beerRepository.deleteAll();
		Page<BeerDto> list = beerController.listBeers(null, null, null, null, null);
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
	@Transactional
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
	@Transactional
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
	@Transactional
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
	@WithMockUser(username = "user1", password = "password")
	void patchBeerByIdWithInvalidName() throws Exception{
		Beer beer = beerRepository.findAll().get(0);
		beer.setBeerName("hefsheeeeeeeeeeeeeeeeeeeeeeeehduesfhueeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
		BeerDto dto = beerMapper.beerToBeerDto(beer);

		mockMvc.perform(
				patch(BEER_PATH_ID, beer.getId())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto))
			)
			.andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
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
