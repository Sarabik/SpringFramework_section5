package com.springframework.section5.controller;

import com.springframework.section5.model.Beer;
import com.springframework.section5.service.BeerService;
import com.springframework.section5.service.BeerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

	// test data
	List<Beer> list = new BeerServiceImpl().listBeers();

	@Autowired
	MockMvc mockMvc;

	@MockBean
	BeerService beerService;

	@Test
	void whenSuccessfullyGetBeerById() throws Exception {

		Beer testBeer = list.get(0);

		when(beerService.getBeerById(testBeer.getId())).thenReturn(testBeer);

		mockMvc.perform(
				get("/api/v1/beer/" + testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
			.andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
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
}