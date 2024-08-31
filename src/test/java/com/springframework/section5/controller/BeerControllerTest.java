package com.springframework.section5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.entity.BeerStyle;
import com.springframework.section5.mapper.BeerMapper;
import com.springframework.section5.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.springframework.section5.controller.BeerController.BEER_PATH;
import static com.springframework.section5.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

	List<BeerDto> list;
	BeerDto beerDto;

	@BeforeEach
	void setUp() {
		BeerDto beerDto1 = BeerDto.builder()
			.id(UUID.randomUUID())
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
			.beerName("Sunshine City")
			.beerStyle(BeerStyle.IPA)
			.upc("12356")
			.price(new BigDecimal("13.99"))
			.quantityOnHand(144)
			.createdDate(LocalDateTime.now())
			.updateDate(LocalDateTime.now())
			.build();

		beerDto = beerDto1;
		list = List.of(beerDto1, beerDto2, beerDto3);
	}

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	BeerService beerService;

	@Captor
	ArgumentCaptor<UUID> captorUUID = ArgumentCaptor.forClass(UUID.class);

	@Captor
	ArgumentCaptor<BeerDto> captorBeer = ArgumentCaptor.forClass(BeerDto.class);

	@Test
	void testCreateNewBeer() throws Exception {

		when(beerService.saveBeer(any(BeerDto.class))).thenReturn(beerDto);

		mockMvc.perform(
			post(BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerDto))
		)
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void testCreateBeerNullRequiredFields() throws Exception {
		BeerDto dto = new BeerDto();

		when(beerService.saveBeer(any(BeerDto.class))).thenReturn(beerDto);

		MvcResult result = mockMvc.perform(
				post(BEER_PATH)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto))
			)
			.andExpect(status().isBadRequest())
			.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		System.out.println(responseBody);
		assertThat(responseBody).contains("beerName", "upc", "beerStyle", "price");
	}

	@Test
	void whenSuccessfullyGetBeerById() throws Exception {

		when(beerService.getBeerById(beerDto.getId())).thenReturn(beerDto);

		mockMvc.perform(
				get(BEER_PATH_ID, beerDto.getId())
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(beerDto.getId().toString())))
			.andExpect(jsonPath("$.beerName", is(beerDto.getBeerName())));
	}

	@Test
	void whenBeerByIdNotFound() throws Exception {

		when(beerService.getBeerById(any(UUID.class))).thenThrow(new NotFoundException());

		mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID()))
			.andExpect(status().isNotFound())
			.andExpect(result ->
				assertInstanceOf(NotFoundException.class, result.getResolvedException()));
	}

	@Test
	void whenSuccessfullyGetListOfBeers() throws Exception {

		Page<BeerDto> page = new PageImpl<>(list);

		when(beerService.listBeers(null, null, null, null, null))
			.thenReturn(page);

		mockMvc.perform(
			get(BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.totalElements", is(3)));

	}

	@Test
	void updateBeerById() throws Exception {
		mockMvc.perform(
			put(BEER_PATH_ID, beerDto.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerDto))
		)
			.andExpect(status().isNoContent());

		verify(beerService, times(1))
							.updateBeerById(any(UUID.class), any(BeerDto.class));
	}

	@Test
	void testWhenUpdatedBeerHasInvalidFields() throws Exception {
		BeerDto dto = new BeerDto();
		dto.setPrice(BigDecimal.valueOf(-0.3));
		MvcResult result = mockMvc.perform(
				put(BEER_PATH_ID, beerDto.getId())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(dto))
			)
			.andExpect(status().isBadRequest())
			.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		System.out.println(responseBody);
		assertThat(responseBody).contains("beerName", "upc", "beerStyle", "price");
	}

	@Test
	void deleteBeerById() throws Exception {
		mockMvc.perform(
			delete(BEER_PATH_ID, beerDto.getId())
				.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isNoContent());

		verify(beerService, times(1)).deleteBeerById(captorUUID.capture());
		assertThat(beerDto.getId()).isEqualTo(captorUUID.getValue());
	}

	@Test
	void patchBeerById() throws Exception{

		BeerDto newBeerDto = new BeerDto();
		newBeerDto.setBeerName("New name");
		newBeerDto.setId(beerDto.getId());

		mockMvc.perform(
				patch(BEER_PATH_ID, newBeerDto.getId())
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(newBeerDto))
			)
			.andExpect(status().isNoContent());

		verify(beerService, times(1))
			.patchBeerById(captorUUID.capture(), captorBeer.capture());
		assertThat(beerDto.getId()).isEqualTo(captorUUID.getValue());
		assertThat(newBeerDto.getBeerName()).isEqualTo(captorBeer.getValue().getBeerName());
	}
}