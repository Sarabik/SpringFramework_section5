package com.springframework.section5.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.service.BeerService;
import com.springframework.section5.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.springframework.section5.controller.BeerController.BEER_PATH;
import static com.springframework.section5.controller.BeerController.BEER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
		list = new BeerServiceImpl().listBeers();
		beerDto = list.get(0);
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
	void whenSuccessfullyGetBeerById() throws Exception {

		when(beerService.getBeerById(beerDto.getId())).thenReturn(Optional.of(beerDto));

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

		when(beerService.getBeerById(any(UUID.class))).thenReturn(Optional.empty());

		mockMvc.perform(get(BEER_PATH_ID, UUID.randomUUID()))
			.andExpect(status().isNotFound())
			.andExpect(result ->
				assertInstanceOf(NotFoundException.class, result.getResolvedException()));
	}

	@Test
	void whenSuccessfullyGetListOfBeers() throws Exception {

		when(beerService.listBeers()).thenReturn(list);

		mockMvc.perform(
			get(BEER_PATH)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)));

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