package com.springframework.section5.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.section5.model.Beer;
import com.springframework.section5.service.BeerService;
import com.springframework.section5.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

	List<Beer> list;
	Beer beer;

	@BeforeEach
	void setUp() {
		list = new BeerServiceImpl().listBeers();
		beer = list.get(0);
	}

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	BeerService beerService;

	@Test
	void testCreateNewBeer() throws Exception {

		when(beerService.saveBeer(any(Beer.class))).thenReturn(beer);

		mockMvc.perform(
			post("/api/v1/beer")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer))
		)
			.andExpect(status().isCreated())
			.andExpect(header().exists("Location"));
	}

	@Test
	void whenSuccessfullyGetBeerById() throws Exception {

		when(beerService.getBeerById(beer.getId())).thenReturn(beer);

		mockMvc.perform(
				get("/api/v1/beer/" + beer.getId())
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(beer.getId().toString())))
			.andExpect(jsonPath("$.beerName", is(beer.getBeerName())));
	}

	@Test
	void whenBeerByIdNotFound() throws Exception {

		mockMvc.perform(
				get("/api/v1/beer/" + UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isNotFound());
	}

	@Test
	void whenSuccessfullyGetListOfBeers() throws Exception {

		when(beerService.listBeers()).thenReturn(list);

		mockMvc.perform(
			get("/api/v1/beer")
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()", is(3)));

	}

	@Test
	void updateBeerById() throws Exception {
		mockMvc.perform(
			put("/api/v1/beer/" + beer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beer))
		)
			.andExpect(status().isNoContent());

		verify(beerService, times(1))
							.updateBeerById(any(UUID.class), any(Beer.class));
	}
}